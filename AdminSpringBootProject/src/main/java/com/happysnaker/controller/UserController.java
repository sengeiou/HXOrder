package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.User;
import com.happysnaker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
@RestController
public class UserController extends BaseController {
    @Autowired
    private UserService service;

    @GetMapping("/admin/list")
    public String getAdminList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = getPaginationInfo(request, USER_ENABLE_STATUS_PARAM);
        if (map == null) {
            return super.setCodeAndReturnMsg(response, PARAM_ERR_CODE, PARAM_ERR_MSG);
        }
        int pageNum = (int) map.get(PAGE_NUM_PARAM);
        int pageSize = (int) map.get(PAGE_SIZE_PARAM);
        String keyword = (String) map.get(KEY_WORD_PARAM);
        Integer status = (Integer) map.get(USER_ENABLE_STATUS_PARAM);
        JSONObject object = new JSONObject();
        object.put("total", service.getSize());
        object.put("arrays", service.getDishListByPagination(pageNum, pageSize, keyword, status));
        return object.toJSONString();
    }

    @PostMapping("/admin/update")
    public String updateAdmin(@RequestBody User user, HttpServletResponse response) {
        try {
            service.updateUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, "服务器无法理解该请求");
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/admin/delete")
    public  String deleteAdmin(HttpServletRequest request, HttpServletResponse response) {

        try {
            Integer id = Integer.parseInt(request.getParameter(USER_ID_PARAM));
            service.deleteUser(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PARAM_ERR_MSG);
        } catch (UpdateException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, "不存在该记录！");
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/admin/add")
    public String addAdmin(@RequestBody User user, HttpServletResponse response) {
        try {
            service.addUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, "服务器无法理解该请求");
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/admin/updateStatus")
    private String updateStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            Integer id = Integer.parseInt(request.getParameter(USER_ID_PARAM));
            Integer status = Integer.parseInt(request.getParameter(USER_ENABLE_STATUS_PARAM));
            service.updateStatus(id, status);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PARAM_ERR_MSG);
        }
        return SUCCESS_MSG;
    }
}
