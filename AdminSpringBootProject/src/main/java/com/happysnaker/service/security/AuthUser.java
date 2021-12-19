package com.happysnaker.service.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements UserDetails {
    private int uid;
    private String username;
    private String password;
    private boolean isEnable;
    private Collection<? extends GrantedAuthority> getAuthorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        System.out.println("getunm");
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        System.out.println("账号没过期");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        System.out.println("账号没锁定？");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        System.out.println("Z");
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
}
