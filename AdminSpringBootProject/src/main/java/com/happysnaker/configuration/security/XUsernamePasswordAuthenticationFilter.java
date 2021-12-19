package com.happysnaker.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happysnaker.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 重写UsernamePasswordAuthenticationFilter过滤器
 */
public class XUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String POST_TYPE = "application/x-www-form-urlencoded";

    @Autowired
    private AuthService service;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)
                || request.getContentType().equals(POST_TYPE)) {

            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            //取 authenticationBean
            Map<String, String> authenticationBean = null;

            try (InputStream is = request.getInputStream()) {
                authenticationBean = mapper.readValue(is, Map.class);
            } catch (IOException e) {
                e.getMessage();
            }
            try {
                if (!authenticationBean.isEmpty()) {
                    //获得账号、密码
                    String username = authenticationBean.get(SPRING_SECURITY_FORM_USERNAME_KEY);
                    String password = authenticationBean.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
                    //可以验证账号、密码
                    System.out.println("username = " + username);
                    System.out.println("password = " + password);
                    if (!service.hasUser(username)) {
                        throw new Exception(String.format("不存在该用户 %s", username));
                    }

                    authRequest = new UsernamePasswordAuthenticationToken(username, password);
                    setDetails(request, authRequest);
                    return this.getAuthenticationManager().authenticate(authRequest);
                }
            } catch (Exception e) {
                System.out.println("P抛出了异常 " + e.getMessage());
            }
        }
        System.out.println("P 发生了错误！");
        //验证不通过，设置状态码
        response.setStatus(401);
        return null;
    }
}
