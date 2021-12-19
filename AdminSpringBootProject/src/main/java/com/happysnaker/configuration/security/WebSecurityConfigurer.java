package com.happysnaker.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * Security授权配置主文件
 * @author happysnakers
 */
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    //登录成功处理器
    @Autowired
    private XAuthenticationSuccessHandler xAuthenticationSuccessHandler;

    @Autowired
    private XAuthenticationFailureHandler xAuthenticationFailureHandler;

    @Autowired
    private XAuthenticationEntryPoint xAuthenticationEntryPoint;

    @Autowired
    private XAccessDeniedHandler xAccessDeniedHandler;

    @Autowired
    private XTokenOncePerRequestFilter xOncePerRequestFilter;

    @Autowired
    private XLogOutOncePerRequestFilter xLogOutHandler;

    @Autowired
    private XLogoutSuccessHandler xLogoutSuccessHandler;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    XUsernamePasswordAuthenticationFilter xUsernamePasswordAuthenticationFilter() throws Exception {
        XUsernamePasswordAuthenticationFilter filter = new XUsernamePasswordAuthenticationFilter();
        //成功后处理
        filter.setAuthenticationSuccessHandler(xAuthenticationSuccessHandler);
           //失败后处理
        filter.setAuthenticationFailureHandler(xAuthenticationFailureHandler);

        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //解决跨域问题。cors 预检请求放行,让Spring security 放行所有preflight request
        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        //让Security永远不会创建HttpSession，它不会使用HttpSession来获取SecurityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().headers().cacheControl();


        //动态鉴权，放行登录登出操作
        http.authorizeRequests()
//                .antMatchers("/login.html", "/login", "/logout").permitAll()
                .anyRequest().access("@dynamicPermission.checkPermission(request, authentication)");

        //拦截账号、密码。
        http.addFilterAt(xUsernamePasswordAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(xOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //拦截 SpringBoot 默认的登出过滤器，默认的登出过滤器会帮我们清除 session 会话、清除用户上下文信息，并且重定向登录 html 页面，因此默认的过滤器会返回 302 状态码，这不是我们想要的，拦截并且设置用户的登录状态，同时配置登出成功处理器
        http.addFilterBefore(xLogOutHandler, LogoutFilter.class).logout().logoutSuccessHandler(xLogoutSuccessHandler);

        http.exceptionHandling().authenticationEntryPoint(xAuthenticationEntryPoint).accessDeniedHandler(xAccessDeniedHandler);
    }
}