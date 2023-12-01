package com.baoge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 提供用户信息，这里没有从数据库查询用户信息，在内存中模拟
     * 浏览器访问：http://localhost:8080/login ,进入Security提供的登录页面，输入账号：zs 密码 123 完成登录，登录成功页面显示 “登录成功”
     */
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("zs").password("123").authorities("admin").build());
        return inMemoryUserDetailsManager;
    }

    // 密码编码器：不加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 授权规则配置:
     * SpringSecurity根据我们在WebSecurityConfig中的配置会对除了“/login”之外的资源进行拦截做登录检查，
     * 如果没有登录会跳转到默认的登录页面“/login” 做登录
     * 输入用户名和密码后点击登录，SpringSecurity的拦截器会拦截到登录请求，获取到用户名和密码封装成认证对象(Token对象)，
     * 底层会调用InMemoryUserDetailsService通过用户名获取用户的认证信息(用户名，密码，权限等，这些信息通常是在数据库存储的)
     * 然后执行认证工作：Security把登录请求传入的密码和InMemoryUserDetailsService中加载的用户的密码进行匹配(通过PasswordEncoder)，
     * 匹配成功跳转成功地址，认证失败就返回错误
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()                                     // 授权配置
                .antMatchers("/login").permitAll()  // 登录路径放行
                .anyRequest().authenticated()                        // 其他路径都要认证之后才能访问
                .and().formLogin()                                   // 允许表单登录
                .successForwardUrl("/loginSuccess")                  // 设置登陆成功页
                .and().logout().permitAll()                          // 登出路径放行
                .and().csrf().disable();                             // 关闭跨域伪造检查
    }
}
