package com.happysnaker.controller;

import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.service.UserService;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@RestController
public class UserController extends BaseController {
    private UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/get_user_info")
    public String getUserInfo(HttpServletRequest request) {
        return service.getUserInfo(request.getParameter(USER_ID_PARAM));
    }

    @GetMapping(value = "/get_user_marked_dish")
    public String getUserMarkedDishes(HttpServletRequest request) {
        //不用检查参数， userId 拦截器已经验证了
        String res = service.getUserMarkedDish(request.getParameter(USER_ID_PARAM));
        logInfo(request, "getUserMarkedDish", res);
        return res;
    }

    @GetMapping(value = "/get_collected_stores")
    public String getUserCollectedStores(HttpServletRequest request) {
        //不用检查参数， userId 拦截器已经验证了
        String res = service.getCollectedStores(request.getParameter(USER_ID_PARAM));
        logInfo(request, "getUserCollectedStores", res);
        return res;
    }

    @GetMapping(value = "/get_used_discount_num")
    public String getUsedDiscountNum(HttpServletRequest request) {
        return service.getUsedDiscountCount(request.getParameter(USER_ID_PARAM));
    }

    @PostMapping(value = "/add_user_like_dish")
    public String addUserLikeDish(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(USER_ID_PARAM)) || !VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        int dishId = Integer.parseInt(request.getParameter(DISH_ID_PARAM));
        String res = service.addUserLikeDish(request.getParameter(USER_ID_PARAM), dishId);
        System.out.println("添加用户喜欢成功！" + res);

        if (res != null && res.equals("OK")) {
            //用户点击喜欢可能是非常频繁的，用户是否喜欢一个菜我们已经做了缓存，同理，更新菜品的喜欢数量更应该设置缓存了，毕竟这算是一个大的表，由于用户对菜品喜欢数量的感知通常是不敏感的，我们允许信息具有一定的延时，即等待增加或减少一定数量时才刷新会磁盘，这样可以避免用户无限点击-取消给磁盘造成压力
            //一种常见做法是将任务交给 service 去完成，同时对每个数据库查询同步数据，以缓存为主(普遍做法)
            //但我们不想随众，另一种做法是容忍查询数据的不一致，这种情况下不必对业务层更改，可以在控制层完成
            //当一个菜品的点赞数量非常小时，数量变化是很敏感的，此时阈值应该设置的小一点，反之应该设置的大一点
            //设置菜品的喜欢数量，多个用户同时进入controller，可能出现缓存不一致问题
            synchronized (this) {
                redis.opsForHash().increment(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId, 1);
                int now = (int) redis.opsForHash().get(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId);
                System.out.println("now dishNum == " + now);
                if (now >= DEFAULT_DISH_LIKE_FLUSH_NUM) {
                    System.out.println("更新喜欢数目！" + dishId + now);
                    notifyObservers(0, dishId, now);
                }
                redis.opsForHash().put(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId, 0);
            }
        }
        return res;
    }

    @PostMapping(value = "/remove_user_like_dish")
    public String removeUserLikeDish(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            return null;
        }
        int dishId = Integer.parseInt(request.getParameter(DISH_ID_PARAM));
        String res = service.removeUserLikeDish(request.getParameter(USER_ID_PARAM), dishId);
        if (res != null && res.equals("OK")) {
            synchronized (this) {
                redis.opsForHash().increment(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId, -1);
                int now = (int) redis.opsForHash().get(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId);
                if (now <= -DEFAULT_DISH_LIKE_FLUSH_NUM) {
                    notifyObservers(0, dishId, now);
                }
                redis.opsForHash().put(RedisCacheManager.DISH_LIKE_NUM_CACHE_KEY, dishId, 0);
            }
        }
        return res;
    }

    @PostMapping(value = "/add_user_collected_dish")
    public String addUserCollectedDish(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            return null;
        }

        return service.addUserCollectedDish(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(DISH_ID_PARAM)));
    }

    @PostMapping(value = "/remove_user_collected_dish")
    public String removeUserCollectedDish(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            return null;
        }

        return service.removeUserCollectedDish(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(DISH_ID_PARAM)));
    }

    @PostMapping(value = "/add_user_will_buy_dish")
    public String addUserWillBuyDish(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            return null;
        }

        return service.addUserWillBuyDish(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(DISH_ID_PARAM)));
    }

    @PostMapping(value = "/remove_user_will_buy_dish")
    public String removeUserWillBuyDish(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            return null;
        }

        return service.removeUserWillBuyDish(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(DISH_ID_PARAM)));
    }

    @PostMapping(value = "/add_user_collected_store")
    public String addUserCollectedStore(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            return null;
        }
        return service.addUserCollectedStore(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(STORE_ID_PARAM)));
    }

    @PostMapping(value = "/remove_user_collected_store")
    public String removeUserCollectedStore(HttpServletRequest request) {
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            return null;
        }

        return service.removeUserCollectedStore(request.getParameter(USER_ID_PARAM), Integer.parseInt(request.getParameter(STORE_ID_PARAM)));
    }
}
