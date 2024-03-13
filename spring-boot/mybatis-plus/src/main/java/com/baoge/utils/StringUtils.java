package com.baoge.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * 提取字符串括号里数字
     * @param str
     * @return
     */
    public static List<String> extractNumberInBracket(String str) {
        // 定义正则表达式匹配括号对中的内容
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        List<String> contents = new ArrayList<>();
        // 提取括号对中的内容并打印
        while (matcher.find()) {
            String content = matcher.group();
            if (content.matches("\\d+") && content.length() > 5) {
                contents.add(content);
            }
        }
        return contents;
    }

}
