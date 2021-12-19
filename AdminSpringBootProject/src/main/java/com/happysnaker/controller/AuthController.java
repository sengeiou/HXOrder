package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Role;
import com.happysnaker.service.AuthService;
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
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
@RestController
public class AuthController extends BaseController {
    @Autowired
    private AuthService service;

    @GetMapping("/role/list")
    public String getRoleList(HttpServletRequest request, HttpServletResponse response) {
        super.printParamForDebug(request);
        Map<String, Object> map = getPaginationInfo(request);
        if (map == null) {
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PAGE_NUM_PARAM);
        }
        int pageNum = (int) map.get(PAGE_NUM_PARAM);
        int pageSize = (int) map.get(PAGE_SIZE_PARAM);
        if (pageSize == -1) {
            pageSize = service.getRoleSize();
        }
        String keyword = (String) map.get(KEY_WORD_PARAM);
        JSONObject object = new JSONObject();
        object.put("total", service.getRoleSize());
        object.put("arrays", service.getRoleByPagination(pageNum, pageSize, keyword));
        return object.toJSONString();
    }


    public String getMenuList(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }


    public String getRoleMenuList(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @GetMapping("/admin/role")
    public String getAdminRole(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(USER_ID_PARAM))) {
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PAGE_NUM_PARAM);
        }
        int id = Integer.parseInt(request.getParameter(USER_ID_PARAM));
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getUserRole(id)).toJSONString();
    }

    @PostMapping("/role/update")
    public String updateRole(@RequestBody Role role, HttpServletResponse response) {
        try {
            service.updateRole(role);
        } catch (UpdateException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }


    @PostMapping("/role/add")
    public String addRole(@RequestBody Role role, HttpServletResponse response) {
        try {
            service.addRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }


    @PostMapping("/role/delete")
    public String addRole(int roleId,  HttpServletResponse response) {
        try {
            service.deleteRole(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }

    @GetMapping("/menu/treeList")
    public String getMenuTree(HttpServletResponse response) {
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getMenuTree()).toJSONString();
    }

    @GetMapping("/role/listMenu")
    public String getRoleMenu(int roleId, HttpServletResponse response) {
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getRoleMenu(roleId)).toJSONString();
    }

    @GetMapping("/role/listStore")
    public String getRoleStore(int roleId, HttpServletResponse response) {
        return JSONUtils.listAddToJsonObject(new JSONObject(), service.getRoleStore(roleId)).toJSONString();
    }

    @PostMapping("/role/allocMenu")
    public String allocMenu(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        System.out.println(params);
        try {
            int id = (int) params.get(ROLE_ID_PARAM);
            List<Integer> ids = (List<Integer>) params.get(MENU_ID_LIST_PARAM);
            List<Integer> ids1 = (List<Integer>) params.get(STORE_ID_LIST_PARAM);
            service.addRoleMenu(id, ids, ids1);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PARAM_ERR_MSG);
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/admin/alloc_role")
    public String allocRole(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            int id = (int) params.get(USER_ID_PARAM);
            List<Integer> ids = (List<Integer>) params.get(ROLE_ID_LIST_PARAM);
            service.addUserRole(id, ids);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, PARAM_ERR_CODE, PARAM_ERR_MSG);
        }
        return SUCCESS_MSG;
    }

}
