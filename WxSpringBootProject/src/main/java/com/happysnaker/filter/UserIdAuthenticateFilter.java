package com.happysnaker.filter;

import com.happysnaker.mapper.OrderMapper;
import com.happysnaker.mapper.UserMapper;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证用户 ID 是否存在，验证用户提交的订单是否是自己的订单
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Component("thirdFilter")
public class UserIdAuthenticateFilter extends AbstractFilterChain{
    private String USER_ID = "userId";
    private String ORDER_ID = "orderId";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    public UserIdAuthenticateFilter(@Qualifier("fourthFilter") AbstractFilterChain filterChain) {
        this.myFilterChain = filterChain;
    }

    @Override
    public boolean isRequired(HttpServletRequest request, HttpServletResponse response) {
        return !VerifyUtils.isNullOrEmpty(request.getParameter(USER_ID));
    }

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        String userId = request.getParameter(USER_ID);
        String orderId = request.getParameter(ORDER_ID);
        boolean v = userMapper.hasUser(userId);
        if (!v) {
            response.setStatus(401);
        }
        // 用户提交订单，订单中的 userId 必须等于 token 标识的 userId
        if (!VerifyUtils.isNullOrEmpty(orderId)) {
            v = orderMapper.queryOrder(orderId).getUserId().equals(userId);
        }
        return v;
    }
}
