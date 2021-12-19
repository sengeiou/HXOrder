package com.happysnaker.filter;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * 编码过滤器
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Component("fourthFilter")
public class CharacterEncodingFilter extends AbstractFilterChain{
    @Override
    public boolean isRequired(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
