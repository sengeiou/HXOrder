package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.pojo.Combo;
import com.happysnaker.pojo.ComboDish;
import com.happysnaker.service.ComboService;
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
 * @date 2021/12/7
 * @email happysnaker@foxmail.com
 */
@RestController
public class ComboController extends BaseController {
    @Autowired
    private ComboService service;

    @PostMapping("/combo/add")
    public String addCombo(@RequestBody Combo combo, HttpServletResponse response) {
//        System.out.println(map);
        System.out.println("combo==" + combo);
        try {
            service.addCombo(combo);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
        }
        return SUCCESS_MSG;
    }

    @GetMapping("/combo/list")
    public String getComboList(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        System.out.println(VerifyUtils.class);
        if (!VerifyUtils.allIsNumber(request.getParameter(PAGE_NUM_PARAM), request.getParameter(PAGE_SIZE_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        int pageNum = Integer.parseInt(request.getParameter(PAGE_NUM_PARAM));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE_PARAM));

        String keyword = null;
        Integer publishStatus = null;
        if (!VerifyUtils.isNullOrEmpty(request.getParameter(KEY_WORD_PARAM))) {
            keyword = request.getParameter(KEY_WORD_PARAM);
        }
        if (VerifyUtils.isNumber(request.getParameter(COMBO_PUBLISH_STATUS_PARAM))) {
            publishStatus = Integer.parseInt(request.getParameter(COMBO_PUBLISH_STATUS_PARAM));
        }

        JSONObject object = new JSONObject();
        object.put("total", service.getComboSize());
        return JSONUtils.listAddToJsonObject(object, service.getComboListByPagination(pageNum, pageSize, keyword, publishStatus)).toJSONString();
    }

    @PostMapping("/combo/update_publish_status")
    public String updatePublishStatus(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        String values = request.getParameter(COMBO_ID_LIST_PARAM);
        System.out.println(values);
        if (!VerifyUtils.allIsNumberList(request.getParameter(COMBO_ID_LIST_PARAM), request.getParameter(COMBO_PUBLISH_STATUS_PARAM))) {
            System.out.println("zz" + request.getParameter(COMBO_PUBLISH_STATUS_PARAM));
            response.setStatus(PARAM_ERR_CODE);
            return null;
        }
        System.out.println("到达P");
        List<Integer> dishIds = VerifyUtils.string2NumberList(request.getParameter(COMBO_ID_LIST_PARAM));
        int val = Integer.parseInt(request.getParameter(COMBO_PUBLISH_STATUS_PARAM));
        service.updateComboStatus(dishIds,  val);
        System.out.println("fuwuwanc");
        return "ok";
    }

    @GetMapping("/combo/combo_dish_list")
    public String getComboDishList(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(COMBO_ID_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        int id = Integer.parseInt(request.getParameter(COMBO_ID_PARAM));
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getComboDishList(id)).toJSONString();
    }

    @PostMapping("/combo/update_combo_dish")
    public String updateComboDish(@RequestBody List<ComboDish> cs, HttpServletResponse response) {
        System.out.println(cs);
        try {
            service.updateComboDish(cs);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
            return NOT_ACCEPTABLE_MSG;
        }
        return SUCCESS_MSG;
    }

    @GetMapping("/combo/get_combo")
    public String getCombo(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        if (!VerifyUtils.isNumber(request.getParameter(COMBO_ID_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        int id = Integer.parseInt(request.getParameter(COMBO_ID_PARAM));
        return JSONObject.toJSONString(service.getCombo(id));
    }

    @PostMapping("/combo/update")
    public String updateCombo(@RequestBody Combo combo, HttpServletResponse response) {
        System.out.println("combo==" + combo);
        try {
            service.updateCombo(combo);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/combo/delete_combo_dish")
    public String deleteComboDish(@RequestBody ComboDish c, HttpServletResponse response) {
        System.out.println(c);
        try {
            service.deleteComboDish(c);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(NOT_ACCEPTABLE_CODE);
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/combo/delete")
    public String updateComboDish(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumberList(request.getParameter(COMBO_ID_LIST_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(COMBO_ID_LIST_PARAM));
        service.deleteCombo(ids);
        return SUCCESS_MSG;
    }
}
