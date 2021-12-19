package com.happysnaker.service.impl;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.pojo.Combo;
import com.happysnaker.pojo.ComboDish;
import com.happysnaker.service.ComboService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/7
 * @email happysnaker@foxmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ComboServiceImpl extends BaseService implements ComboService {

    private List<Combo> getStatusFromRedis(List<Combo> combos) {
        if (!redisUtils.hasKey(RedisCacheManager.COMBO_PUBLISH_STATUS_KEY)) {
            redisUtils.initRedisComboPublishStatusCache(comboMapper.queryComboInfo());
        }
        try {
            for (Combo combo : combos) {
                System.out.println(combo.getId() + " --> redis --> " + redisUtils.getBit(RedisCacheManager.COMBO_PUBLISH_STATUS_KEY, combo.getId()));
                combo.setPublishStatus(redisUtils.getBit(RedisCacheManager.COMBO_PUBLISH_STATUS_KEY, combo.getId()));
            }
            return combos;
        } catch (Exception e) {
            e.printStackTrace();
            //重新尝试
            return getStatusFromRedis(combos);
        }
    }

    @Override
    public Combo getCombo(int id) {
        return comboMapper.getComboById(id);
    }

    @Override
    public void deleteComboDish(ComboDish c) {
        int row = comboMapper.deleteComboDishItem(c);
        if (row == 0) {
            throw new RuntimeException("不存在该记录");
        }
    }

    @Override
    public List<Combo> getComboListByPagination(int pageNum, int pageSize, String keyword, Integer status) {
        int offset = (pageNum - 1)* pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        //菜品状态以缓存中为主，因此我们不从数据库中读 status
        List<Combo> combos = getStatusFromRedis(comboMapper.queryComboByPagination(offset, pageSize, keyword, null));
        //如果用户确实查询了上架状态，则根据缓存中上架状态进行过滤
        if (status != null) {
            combos = combos.stream().filter((combo)-> {
                return redisUtils.getBit(
                        RedisCacheManager.COMBO_PUBLISH_STATUS_KEY,
                        combo.getId()
                ) == status;
            }).collect(Collectors.toList());
        }
        return combos;
    }

    @Override
    public int getComboSize() {
        return comboMapper.queryComboTotalSize();
    }

    @Override
    public int addCombo(Combo combo){
        int row = comboMapper.insertCombo(combo);
        int id = comboMapper.getIdByName(combo.getName());
        //这个在前端做的，但是前端 JS SPLICE 一指分割不了空格
        if (combo.getTags().size() == 1) {
            System.out.println("qqqqqq=" + combo.getTags().get(0));
            combo.setTags(Arrays.asList(combo.getTags().get(0).split("\\s")));
        }
        for (String tag : combo.getTags()) {
            System.out.println(id + " tt = " + tag);
            dishMapper.insertDishTag(id, tag);
        }
        for (Integer classificationId : combo.getClassificationIds()) {
            dishMapper.insertDishClassification(id, classificationId);
        }
        for (ComboDish comboDish : combo.getComboDish()) {
            comboDish.setComboId(id);
            comboMapper.insertComboDish(comboDish);
        }
        combo.getDiscount().setDishId(id);
        discountMapper.insertDiscount(combo.getDiscount());
        return id;
    }

    @Override
    public void deleteCombo(int comboId) {
        comboMapper.deleteCombo(comboId);
        comboMapper.deleteComboDish(comboId);
        discountMapper.deleteDiscount(comboId);
        dishMapper.deleteDishClassification(comboId);
        dishMapper.deleteDishStock(comboId);
        dishMapper.deleteDishTag(comboId);
    }

    @Override
    public void updateCombo(Combo combo) {
        deleteCombo(combo.getId());
        addCombo(combo);
    }

    @Override
    public void updateComboStatus(int id, int val) {
        redisUtils.setBit(RedisCacheManager.COMBO_PUBLISH_STATUS_KEY, id, val);
        System.out.println(id + "-->redisupdate" + val + " -;" + redisUtils.getBit(RedisCacheManager.COMBO_PUBLISH_STATUS_KEY, id));
    }

    @Override
    public List<ComboDish> getComboDishList(int id) {
        return comboMapper.queryComboDishById(id);
    }

    @Override
    public void updateComboDish(List<ComboDish> comboDishes) {
        for (ComboDish comboDish : comboDishes) {
            int row = comboMapper.updateComboDish(comboDish);
            if (row == 0) {
                throw new RuntimeException("不存在该套餐菜品记录 " + comboDish);
            }
        }
    }
}
