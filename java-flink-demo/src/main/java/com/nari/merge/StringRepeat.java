package com.nari.merge;

public class StringRepeat {
    public static String repeat(String original, int count) {
        if (original == null) {
            return null;
        }
        if (count <= 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(original.length() * count);
        for (int i = 0; i < count; i++) {
            builder.append(original);
        }
        return builder.toString();
    }
 
    public static void main(String[] args) {
        String str = "abc";
        int times = 3;
        String repeated = repeat(str, times); // 自定义的repeat方法
        System.out.println(repeated); // 输出: abcabcabc
    }
}