package com.baoge.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Desc: 通过这个类将 string 的 token 转成 AuthenticationToken，shiro 才能接收
 * 由于Shiro不能识别字符串的token，所以需要对其进行一下封装
 * shiro 在没有和 jwt 整合之前，用户的账号密码被封装成了 UsernamePasswordToken 对象，
 * UsernamePasswordToken 其实是 AuthenticationToken 的实现类。
 * 这里既然要和 jwt 整合，JWTFilter 传递给 Realm 的 token 必须是 AuthenticationToken 的实现类。
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
