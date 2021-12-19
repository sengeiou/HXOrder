package com.happysnaker.service.impl;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.pojo.Classification;
import com.happysnaker.pojo.Dish;
import com.happysnaker.pojo.Store;
import com.happysnaker.service.DishService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@Service
public class DishServiceImpl extends BaseService implements DishService {

    private List<Dish> getStatusFromRedis(List<Dish> dishes) {
        List<Dish> dishesDb = dishMapper.queryDishes();
        if (!redisUtils.hasKey(RedisCacheManager.DISH_PUBLISH_STATUS_KEY)) {
            redisUtils.initRedisDishPublishStatusCache(dishesDb);
        }
        if (!redisUtils.hasKey(RedisCacheManager.DISH_RECOMMEND_STATUS_KEY)) {
            System.out.println("缓存失效！");
            redisUtils.initRedisDishRecommendStatusCache(dishesDb);
        }
        if (!redisUtils.hasKey(RedisCacheManager.DISH_NEW_STATUS_KEY)) {
            redisUtils.initRedisDishNewStatusCache(dishesDb);
        }
        try {
            for (Dish dish : dishes) {
                dish.setPublishStatus(redisUtils.getBit(RedisCacheManager.DISH_PUBLISH_STATUS_KEY, dish.getId()));
                dish.setRecommendStatus(redisUtils.getBit(RedisCacheManager.DISH_RECOMMEND_STATUS_KEY, dish.getId()));
                dish.setNewStatus(redisUtils.getBit(RedisCacheManager.DISH_NEW_STATUS_KEY, dish.getId()));
            }
            return dishes;
        } catch (Exception e) {
            e.printStackTrace();
            //重新尝试
            return getStatusFromRedis(dishes);
        }
    }


    @Override
    public List<Dish> getDishListByPagination(int pageNum, int pageSize, String keyword, Integer status1, Integer status2, Integer status3) {
        int offset = (pageNum - 1)* pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        //菜品状态以缓存中为主，因此我们不从数据库中读 status
        List<Dish> dishes = getStatusFromRedis(dishMapper.queryDishByPagination(offset, pageSize, keyword));
        //如果用户确实查询了菜品状态，则根据缓存中上架状态进行过滤
        if (status1 != null || status2 != null || status3 != null) {
            dishes = dishes.stream().filter((dish)-> {
                return  (status1 == null || redisUtils.getBit(RedisCacheManager.DISH_PUBLISH_STATUS_KEY,dish.getId()) == status1)
                        &&
                        (status2 == null || redisUtils.getBit(RedisCacheManager.DISH_RECOMMEND_STATUS_KEY, dish.getId()) == status2)
                        &&
                        (status3 == null || redisUtils.getBit(RedisCacheManager.DISH_NEW_STATUS_KEY, dish.getId()) == status3);
            }).collect(Collectors.toList());
        }
        return dishes;
    }

    @Override
    public int getDishSize() {
        return dishMapper.queryDishTotalSize();
    }

    @Override
    public Dish getDish(int id) {
        return dishMapper.queryDishById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDish(Dish dish) {
        deleteDish(dish.getId());
        addDish(dish);
    }

    @Override
    public void updateDishStatus(int dishId, int val , int options) {
        String[] keyMap = new String[]{
                "占位符",
                RedisCacheManager.DISH_PUBLISH_STATUS_KEY,
                RedisCacheManager.DISH_RECOMMEND_STATUS_KEY,
                RedisCacheManager.DISH_NEW_STATUS_KEY
        };
        System.out.println("val == " + val + "keyMap[options]");
        redisUtils.setBit(keyMap[options], dishId, val);
        System.out.println(redisUtils.getBit(keyMap[options], dishId));
    }

    @Override
    public List<Map> getDishStockList(int dishId) {
        List ans = new ArrayList();
        List<Store> stores = storeMapper.getStoreInfo();
        for (Store store : stores) {
            Integer stock = dishMapper.getTheDishInventory(store.getId(), dishId);
            Map map = new HashMap(3);
            map.put("storeId", store.getId());
            map.put("storeName", store.getName());
            map.put("stock", stock);
            map.put("dishId", dishId);
            ans.add(map);
        }
        return ans;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDishStockList(List<Map> maps) {
        Function<Object, Integer> switchToInt = new Function<Object, Integer>() {
            @Override
            public Integer apply(Object o) {
                Integer ans = null;
                try {
                    ans = (Integer) o;
                } catch (ClassCastException e) {
                    if (!(o instanceof String)) {
                        throw new RuntimeException("更新菜品库存出错，需要接受整数");
                    }
                    ans = Integer.parseInt((String) o);
                    System.out.println(ans);
                }
                return ans;
            }
        };
        try {
            for (Map map : maps) {
                Object storeId = map.get("storeId"), dishId = map.get("dishId"), stock = map.get("stock");
                int row = dishMapper.updateDishInventory(switchToInt.apply(storeId), switchToInt.apply(dishId), switchToInt.apply(stock));
                if (row == 0) {
                    throw  new RuntimeException("更新失败，不存在该记录");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //更新失败，可能是个新值，尝试插入，如插入失败则回滚全部
            for (Map map : maps) {
                Object storeId = map.get("storeId"), dishId = map.get("dishId"), stock = map.get("stock");
                dishMapper.insertDishInventory(switchToInt.apply(storeId), switchToInt.apply(dishId), switchToInt.apply(stock));
            }
        }
    }

    @Override
    public List<Classification> getClassificationList() {
        return dishMapper.getClassificationList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addDish(Dish dish) {
        dishMapper.insertDish(dish);
        int id = dishMapper.getIdByName(dish.getName());
        System.out.println(id);
        //这个在前端做的，但是前端 JS SPLICE 一指分割不了空格
        if (dish.getTags().size() == 1) {
            System.out.println("qqqqqq=" + dish.getTags().get(0));
            dish.setTags(Arrays.asList(dish.getTags().get(0).split("\\s")));
        }
        for (String tag : dish.getTags()) {
            System.out.println(tag);
            dishMapper.insertDishTag(id, tag);
        }
        for (Integer cId : dish.getClassificationIds()) {
            dishMapper.insertDishClassification(id, cId);
        }
        dish.getDiscount().setDishId(id);
        discountMapper.insertDiscount(dish.getDiscount());
        return id;
    }

    @Override
    public void deleteDish(int dishId) {
        // 这里可以先删除 dish 表，然后利用MQ异步删除其他
        dishMapper.deleteDish(dishId);
        discountMapper.deleteDiscount(dishId);
        dishMapper.deleteDishClassification(dishId);
        dishMapper.deleteDishStock(dishId);
        dishMapper.deleteDishTag(dishId);
    }

}
