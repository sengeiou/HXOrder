package com.happysnaker.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 参数管理器，统一管理网络参数
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
public class BaseController implements Serializable {
    protected final String PAGE_NUM_PARAM = "pageNum";
    protected final String PAGE_SIZE_PARAM  = "pageSize";
    protected final String DISH_ID_PARAM  = "dishId";
    protected final String DISH_PARAM  = "dish";
    protected final String COMBO_ID_PARAM  = "comboId";
    protected final String STORE_ID_PARAM  = "storeId";
    protected final String DISH_ID_LIST_PARAM  = "dishIds";
    protected final String COMBO_ID_LIST_PARAM  = "comboIds";
    protected final String USER_ID_LIST_PARAM  = "userIds";
    protected final String USER_ID_PARAM  = "userId";
    protected final String STORE_ID_LIST_PARAM  = "storeIds";
    protected final String STORE_WORKING_STATUS_PARAM = "working";
    protected final String STORE_TAKEOUT_STATUS_PARAM = "takeoutStatus";
    protected final String COMBO_PUBLISH_STATUS_PARAM  = "publishStatus";
    protected final String DISH_PUBLISH_STATUS_PARAM  = "publishStatus";
    protected final String DISH_RECOMMEND_STATUS_PARAM  = "recommendStatus";
    protected final String DISH_NEW_STATUS_PARAM  = "newStatus";
    protected final String KEY_WORD_PARAM = "keyword";
    protected final String DISH_STOCK_PARAM = "dishStock";
    protected final String DISCOUNT_TYPE_PARAM = "discountType";
    protected final String USER_ENABLE_STATUS_PARAM = "status";
    protected final String ROLE_ID_PARAM = "roleId";
    protected final String MENU_ID_LIST_PARAM = "menuIds";
    protected final String ROLE_ID_LIST_PARAM = "roleIds";

    protected final int PARAM_ERR_CODE = 400;
    protected final String PARAM_ERR_MSG = "{code:400,msg:参数错了大哥}";
    protected final String SERVER_ERR_MSG = "{code:500,msg:服务器炸了}";
    protected final String SUCCESS_MSG = "{code:200,msg:服务成功}";
    protected final String NOT_ACCEPTABLE_MSG = "未知错误，拒绝服务";
    protected final int NOT_ACCEPTABLE_CODE = 406;

    @Qualifier("myRedis")
    @Autowired
    protected RedisTemplate redis;

    protected void printParamForDebug(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> it : map.entrySet()) {
            System.out.print(it.getKey() + ":--> ");
            for (String s : it.getValue()) {
                System.out.print(s + "< ");
            }
        }
        System.out.println();
    }

    protected Map<String, Object> getPaginationInfo(HttpServletRequest request, String... ints) {
        this.printParamForDebug(request);
        if (!VerifyUtils.allIsNumber(request.getParameter(PAGE_NUM_PARAM), request.getParameter(PAGE_SIZE_PARAM))) {
            return null;
        }
        int pageNum = Integer.parseInt(request.getParameter(PAGE_NUM_PARAM));
        int pageSize = Integer.parseInt(request.getParameter(PAGE_SIZE_PARAM));
        String keyword = null;
        if (!VerifyUtils.isNullOrEmpty(request.getParameter(KEY_WORD_PARAM))) {
            keyword = request.getParameter(KEY_WORD_PARAM);
        }
        Map<String, Object> map = new HashMap<>();
        map.put(PAGE_NUM_PARAM, pageNum);
        map.put(PAGE_SIZE_PARAM, pageSize);
        map.put(KEY_WORD_PARAM, keyword);
        for (String anInt : ints) {
            map.put(anInt, null);
            if (VerifyUtils.isNumber(request.getParameter(anInt))) {
                map.put(anInt, Integer.parseInt(request.getParameter(anInt)));
            }
        }
        return map;
    }

    protected String setCodeAndReturnMsg(HttpServletResponse response, int c, String m) {
        Map map = new HashMap(2);
        map.put("code", c);
        map.put("msg", m);
        return JSONObject.toJSONString(map);
    }
}
