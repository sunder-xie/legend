package com.tqmall.legend.web.security;

import com.tqmall.legend.biz.sys.UserInfoService;
import com.tqmall.legend.entity.privilege.ShopManagerLogin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;

public class MyUserDetailServiceImpl implements UserDetailsService {

    Logger logger = LoggerFactory.getLogger(DefaultAccessDeniedHandler.class);

    /**
     * 用户登录表
     */
    @Autowired
    UserInfoService userInfoService;

    //登录验证 account为账号
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        ShopManagerLogin login = null;
        // 查询用户登录信息
        if (!StringUtils.isBlank(account)) {
            login = userInfoService.getLoginByAccount(account);
        }
        if (null == login) {
            return null;
        }
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority auth2 = new SimpleGrantedAuthority("ROLE_USER");
        auths.add(auth2);
        UserDetails user = new User(account, login.getPassword(), true, true, true, true, auths);
        return user;
    }
}
