package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.pojo.Dish;
import com.happysnaker.service.DishService;
import com.happysnaker.utils.JSONUtils;
import com.happysnaker.utils.TxCosUtils;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@RestController
public class DishController extends BaseController {
    @Autowired
    private DishService service;

    @Autowired
    private TxCosUtils cosUtils;

    @PostMapping("/upload_img")
    public String uploadImg(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        return ts.toString().replaceAll(" ", "-");
//        try {
//            String p = cosUtils.uploadFile(file, ts.toString().replaceAll(" ", "-") + ".jpg");
//            return p;
//        } catch (TxCosUploadException e) {
//            e.printStackTrace();
//            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, NOT_ACCEPTABLE_MSG);
//        }
    }


    @GetMapping("/dish/list")
    public String getDishList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = super.getPaginationInfo(request);
        int pageNum = (int) map.get(PAGE_NUM_PARAM);
        int pageSize = (int) map.get(PAGE_SIZE_PARAM);
        String keyword = (String) map.get(KEY_WORD_PARAM);
        Integer publishStatus = null, recommendStatus = null, newStatus = null;
        if (VerifyUtils.isNumber(request.getParameter(DISH_PUBLISH_STATUS_PARAM))) {
            publishStatus = Integer.parseInt(request.getParameter(DISH_PUBLISH_STATUS_PARAM));
        }
        if (VerifyUtils.isNumber(request.getParameter(DISH_RECOMMEND_STATUS_PARAM))) {
            recommendStatus = Integer.parseInt(request.getParameter(DISH_RECOMMEND_STATUS_PARAM));
        }
        if (VerifyUtils.isNumber(request.getParameter(DISH_NEW_STATUS_PARAM))) {
            newStatus = Integer.parseInt(request.getParameter(DISH_NEW_STATUS_PARAM));
        }

        JSONObject object = new JSONObject();
        object.put("total", service.getDishSize());
        return JSONUtils.listAddToJsonObject(object, service.getDishListByPagination(pageNum, pageSize, keyword, publishStatus, recommendStatus, newStatus)).toJSONString();
    }

    @GetMapping("/dish/dish_stock_list")
    public String getDishStockList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println('a');
        super.printParamForDebug(request);
        if (!VerifyUtils.isNumber(request.getParameter(DISH_ID_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        int dishId = Integer.parseInt(request.getParameter(DISH_ID_PARAM));
        List<Map> maps = service.getDishStockList(dishId);
        System.out.println("MMMMM" + maps);
        return JSONUtils.listAddToJsonObject(new JSONObject(), maps).toJSONString();
    }

    @PostMapping("/dish/update_dish_stock")
    public String updateDishStockList(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        List<Map> maps = null;
        try {
            maps = JSONObject.parseObject(request.getParameter(DISH_STOCK_PARAM), List.class);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        try {
            service.updateDishStockList(maps);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            return SERVER_ERR_MSG;
        }
        return null;
    }

    @GetMapping("/dish/classification_list")
    public String getClassificationList(HttpServletRequest request, HttpServletResponse response) {
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getClassificationList()).toJSONString();
    }

    @GetMapping("/dish/get")
    public String getDish(HttpServletRequest request, HttpServletResponse response) {
        int dishId = Integer.parseInt(request.getParameter(DISH_ID_PARAM));
        super.printParamForDebug(request);
        Dish dish = service.getDish(dishId);
        System.out.println(dish);
        return JSONObject.toJSONString(dish);
    }
}
