package com.baoge.controller;

import com.baoge.entity.ConsCurve;
import com.baoge.entity.User;
import com.baoge.entity.UserDO;
import com.baoge.service.UserService;
import com.baoge.utils.ExcelUtils;
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
    public User getUserInfoById(@PathVariable("id") long id) {
        User userDO = userService.getById(id);
        return userDO;
    }

    @GetMapping("/cons")
    public Map<String, List<String>> find() throws Exception {
//        List<String> consList = Arrays.asList("8005583495", "8209000036", "8209000100", "8209000110", "8209000071", "8209000111", "8615100356", "3772889095", "2501000358", "8615000031", "8615000435", "3000659100", "8005584338", "2503012762", "3904565200", "3926031804", "8303602985", "2201000013", "8305301663", "8405002902", "3912551989", "8615200004", "2001001823", "3932236732", "3613940099", "2001001781", "3932661110", "2300661588", "3087976334");
       // 注意： 每次修改 fileName  sheetIndex date
        String fileName = "负荷聚合商参与调峰市场数据清单10-23";
        String sourceFilePath = "D:\\" + fileName + ".xlsx";
        String targetFilePath = "D:\\" + fileName + "-1.xlsx";
        int sheetIndex = 0;
        List<String> consList =
                ExcelUtils.getConsNos(sourceFilePath, 1, 1, sheetIndex);
        String date = "2023-10-23"; // 10:0 15:3 24:6
        Map<String, List<String>> rowDataMap = new LinkedHashMap<>();
        for (String consNo : consList) {
            ConsCurve result = userService.find(consNo, date);
            System.out.println(consNo + ":");
            if (result == null) {
                continue;
            }
            List<String> points = new ArrayList<>();
            for (int i = 1; i <= 96; i++) {
                Field field = ConsCurve.class.getDeclaredField("p" + i);
                field.setAccessible(true);
                Object value = field.get(result);
                points.add(value + "");
//                System.out.println(value);
            }
            rowDataMap.put(consNo, points);
        }
        ExcelUtils.fillData3(sourceFilePath, 1, 1, sheetIndex, rowDataMap, 4,
                targetFilePath);

        return rowDataMap;
    }


    @GetMapping("/user/info")
    public User getUserInfoByUsername(@RequestParam("username") String username,
                                        @RequestParam("type") int type) {
        User user = userService.getByUsername(username, type);
        return user;
    }


}