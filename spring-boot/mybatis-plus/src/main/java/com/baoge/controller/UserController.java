package com.baoge.controller;

import com.baoge.entity.ConsCurve10;
import com.baoge.entity.UserDO;
import com.baoge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/user/info/{id}")
    public UserDO getUserInfoById(@PathVariable("id") long id) {
        UserDO userDO = userService.getById(id);
        return userDO;
    }

    @GetMapping("/cons")
    public Map<String, String> find() throws Exception {
        List<String> consList = Arrays.asList("8005583495", "8209000036", "8209000100", "8209000110", "8209000071", "8209000111", "8615100356", "3772889095", "2501000358", "8615000031", "8615000435", "3000659100", "8005584338", "2503012762", "3904565200", "3926031804", "8303602985", "2201000013", "8305301663", "8405002902", "3912551989", "8615200004", "2001001823", "3932236732", "3613940099", "2001001781", "3932661110", "2300661588", "3087976334");
        String date = "2023-10-24";
        Map<String, String> map = new LinkedHashMap<>();
        for (String consNo : consList) {
            ConsCurve10 result = userService.find(consNo, date);
            System.out.println(consNo + ":");
            if (result == null) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i <= 96; i++) {
                Field field = ConsCurve10.class.getDeclaredField("p" + i);
                field.setAccessible(true);
                Object value = field.get(result);
//                builder.append(value).append(" ");
                System.out.println(value);
            }
//            map.put(consNo, builder.toString());
        }

        return map;
    }


    @GetMapping("/user/info")
    public UserDO getUserInfoByUsername(@RequestParam("username") String username,
                                        @RequestParam("type") int type) {
        UserDO userDO = userService.getByUsername(username, type);
        return userDO;
    }


}