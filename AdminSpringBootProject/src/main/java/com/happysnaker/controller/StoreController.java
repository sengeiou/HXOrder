package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.pojo.Store;
import com.happysnaker.service.StoreService;
import com.happysnaker.utils.JSONUtils;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@RestController
public class StoreController extends BaseController {
    @Autowired
    private StoreService service;

    @GetMapping("/store/get")
    public String getStore(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        System.out.println(VerifyUtils.class);
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        int id = Integer.parseInt(request.getParameter(STORE_ID_PARAM));
        return JSONObject.toJSONString(service.getStore(id));
    }

    @GetMapping("/store/list")
    public String getStoreList(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        System.out.println(VerifyUtils.class);
        if (!VerifyUtils.allIsNumber(request.getParameter(PAGE_NUM_PARAM), request.getParameter(PAGE_SIZE_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        int pageNum = Integer.parseInt(request.getParameter(PAGE_NUM_PARAM));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE_PARAM));
        //查询全部
        if (pageSize == -1) {
            pageSize = service.getStoreSize();
        }
        String keyword = null;
        Integer publishStatus = null;
        if (!VerifyUtils.isNullOrEmpty(request.getParameter(KEY_WORD_PARAM))) {
            keyword = request.getParameter(KEY_WORD_PARAM);
        }
        if (VerifyUtils.isNumber(request.getParameter(DISH_PUBLISH_STATUS_PARAM))) {
            publishStatus = Integer.parseInt(request.getParameter(DISH_PUBLISH_STATUS_PARAM));
        }

        JSONObject object = new JSONObject();
        object.put("total", service.getStoreSize());
        return JSONUtils.listAddToJsonObject(object, service.getStoreListByPagination(pageNum, pageSize, keyword, publishStatus)).toJSONString();
    }

    @PostMapping("/store/add")
    public String addStore(@RequestBody Store store, HttpServletResponse response) {
        try {
            service.addStore(store);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
            return NOT_ACCEPTABLE_MSG;
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/store/update")
    public String updateStore(@RequestBody Store store, HttpServletResponse response) {
        try {
            service.updateStore(store);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
            return NOT_ACCEPTABLE_MSG;
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/store/delete")
    public String deleteStore(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumberList(request.getParameter(STORE_ID_LIST_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PAGE_NUM_PARAM;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(STORE_ID_LIST_PARAM));
        try {
            service.deleteStore(ids);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
            return NOT_ACCEPTABLE_MSG;
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/store/update_working_status")
    public String updateWorkingStatus(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.allIsNumberList(request.getParameter(STORE_ID_LIST_PARAM), request.getParameter(STORE_WORKING_STATUS_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PAGE_NUM_PARAM;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(STORE_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(STORE_WORKING_STATUS_PARAM));
        service.updateStoreStatus(ids, val, 1);
        return SUCCESS_MSG;
    }

    @PostMapping("/store/update_takeout_status")
    public String updateTakeoutStatus(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        if (!VerifyUtils.allIsNumberList(request.getParameter(STORE_ID_LIST_PARAM), request.getParameter(STORE_TAKEOUT_STATUS_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PAGE_NUM_PARAM;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(STORE_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(STORE_TAKEOUT_STATUS_PARAM));
        service.updateStoreStatus(ids, val, 2);
        return SUCCESS_MSG;
    }
}
