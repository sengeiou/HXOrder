package com.happysnaker.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
public abstract class AbstractFilterChain implements Filter {

    AbstractFilterChain myFilterChain;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (isRequired((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)) {
            if (!doFilter((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse)) {
                return;
            }
        }
        if (myFilterChain != null) {
            myFilterChain.doFilter(servletRequest, servletResponse, filterChain);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }



    /**
     * 是否需要处理
     * @param request
     * @param response
     * @return
     */
    public abstract boolean isRequired(HttpServletRequest request, HttpServletResponse response);

    /**
     * 进行过滤
     * @param request
     * @param response
     * @return true 代表进行到下一个过滤链、false 表示到此结束
     */
    public abstract boolean doFilter(HttpServletRequest request, HttpServletResponse response);
}
