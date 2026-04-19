package com.baoge.utils;

import java.util.Base64;
import java.util.Random;

public class NacosSecretKeyGenerator {
    public static void main(String[] args) {
        // 1. 定义原始密钥长度（≥32，此处设为40，可自定义）
        int keyLength = 40;
        // 2. 定义随机字符池（字母+数字+特殊符号，提升安全性）
        String charPool = "zhnyxtghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ01396829!@#$%^&*()_+-=";
        // 3. 生成随机原始密钥
        StringBuilder rawKey = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < keyLength; i++) {
            rawKey.append(charPool.charAt(random.nextInt(charPool.length())));
        }
        // 4. 转Base64编码
        String base64Key = Base64.getEncoder().encodeToString(rawKey.toString().getBytes());
        // 5. 输出结果
        System.out.println("原始密钥（≥32位）：" + rawKey);
        System.out.println("Base64编码后（配置值）：" + base64Key);
    }
}