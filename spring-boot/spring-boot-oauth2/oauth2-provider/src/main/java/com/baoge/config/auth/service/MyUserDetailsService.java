package com.baoge.config.auth.service;

import com.baoge.config.auth.model.MyUserDetails;
import com.baoge.model.User;
import com.baoge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userService.findByAccount(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new MyUserDetails(user);
    }
}
