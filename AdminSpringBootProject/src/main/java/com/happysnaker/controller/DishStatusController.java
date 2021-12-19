package com.happysnaker.controller;

import com.happysnaker.controller.base.BaseController;
import com.happysnaker.pojo.Dish;
import com.happysnaker.service.DishService;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@RestController
public class DishStatusController extends BaseController {
    @Autowired
    private DishService service;

    @PostMapping("/dish/update_publish_status")
    public String updatePublishStatus(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        System.out.println("到达这里");
        String values = request.getParameter(DISH_ID_LIST_PARAM);
        System.out.println(values);
        if (!VerifyUtils.allIsNumberList(request.getParameter(DISH_ID_LIST_PARAM), request.getParameter(DISH_PUBLISH_STATUS_PARAM))) {
            System.out.println("zz" + request.getParameter(DISH_PUBLISH_STATUS_PARAM));
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        System.out.println("到达P");
        List<Integer> dishIds = VerifyUtils.string2NumberList(request.getParameter(DISH_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(DISH_PUBLISH_STATUS_PARAM));
        service.updateDishStatus(dishIds,  val, 1);
        System.out.println("fuwuwanc");
        return "ok";
    }

    @PostMapping("/dish/update_recommend_status")
    public String updateRecommendStatus(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        if (!VerifyUtils.allIsNumberList(request.getParameter(DISH_ID_LIST_PARAM), request.getParameter(DISH_RECOMMEND_STATUS_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        List<Integer> dishIds = VerifyUtils.string2NumberList(request.getParameter(DISH_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(DISH_RECOMMEND_STATUS_PARAM));
        service.updateDishStatus(dishIds,  val, 2);
        return "ok";
    }

    @PostMapping("/dish/update_new_status")
    public String updateNewStatus(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        if (!VerifyUtils.allIsNumberList(request.getParameter(DISH_ID_LIST_PARAM), request.getParameter(DISH_NEW_STATUS_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        List<Integer> dishIds = VerifyUtils.string2NumberList(request.getParameter(DISH_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(DISH_NEW_STATUS_PARAM));
        service.updateDishStatus(dishIds,  val, 3);
        return "ok";
    }


    @PostMapping("/dish/add")
    public String addDish(@RequestBody Dish dish, HttpServletResponse response) throws IOException {
        System.out.println(dish);
        try {
            service.addDish(dish);
        } catch (Exception e) {
            response.setStatus(500);
            e.printStackTrace();
            return SERVER_ERR_MSG;
        }
        return "Ok";
    }

    @PostMapping("/dish/delete")
    public String deleteDish(HttpServletRequest request, HttpServletResponse response) throws IOException {
        super.printParamForDebug(request);
        if (!VerifyUtils.isNumberList(request.getParameter(DISH_ID_LIST_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(DISH_ID_LIST_PARAM));
        try {
            service.deleteDish(ids);
        } catch (Exception e) {
            response.setStatus(NOT_ACCEPTABLE_CODE);
            e.printStackTrace();
        }
        return "OK";
    }

    @PostMapping("/dish/update")
    public String updateDish(@RequestBody Dish dish, HttpServletResponse response) throws IOException {
        System.out.println("upd" + dish);
        try {
            service.updateDish(dish);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
        }
        return "Ok";
    }

}
