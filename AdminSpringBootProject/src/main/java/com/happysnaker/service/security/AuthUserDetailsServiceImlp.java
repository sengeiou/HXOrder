package com.happysnaker.service.security;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.pojo.Role;
import com.happysnaker.pojo.User;
import com.happysnaker.service.base.BaseService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Component
public class AuthUserDetailsServiceImlp extends BaseService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUser(username);
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        for (Role role : authMapper.getUserRole(user.getId())) {
            roles.add(new SimpleGrantedAuthority(role.getName()));
        }
        //启用状态以缓存中为主
        try {
//            redis.delete(RedisCacheManager.USER_ENABLE_STATUS_KEY);
            return new AuthUser(user.getId(), user.getUsername(), user.getPassword(), redisUtils.getBit(RedisCacheManager.USER_ENABLE_STATUS_KEY, (long)user.getId()), roles);
        } catch (Exception e) {
            // 缓存中可能没有 key，尝试重试
            System.out.println("重试");
            redisUtils.initRedisUserEnableStatusCache(userMapper.getUserList());
            return loadUserByUsername(username);
        }
    }
}
