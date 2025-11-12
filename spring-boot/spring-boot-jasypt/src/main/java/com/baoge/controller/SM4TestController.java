package com.baoge.controller;

import com.baoge.config.ConfigService;
import com.baoge.utils.SM4Util;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SM4加解密测试控制器
 */
@RestController
@RequestMapping("/api/sm4")
public class SM4TestController {
    
    @Autowired
    @Qualifier("sm4StringEncryptor")
    private StringEncryptor sm4Encryptor;
    
    @Autowired
    private ConfigService configService;
    
    /**
     * 加密文本
     */
    @PostMapping("/encrypt")
    public Map<String, Object> encrypt(@RequestParam String text) {
        Map<String, Object> result = new HashMap<>();
        try {
            String encrypted = sm4Encryptor.encrypt(text);
            result.put("success", true);
            result.put("original", text);
            result.put("encrypted", encrypted);
            result.put("forConfig", "ENC(" + encrypted + ")");
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
    
    /**
     * 解密文本
     */
    @PostMapping("/decrypt")
    public Map<String, Object> decrypt(@RequestParam String encryptedText) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 处理ENC()格式
            String textToDecrypt = encryptedText;
            if (encryptedText.startsWith("ENC(") && encryptedText.endsWith(")")) {
                textToDecrypt = encryptedText.substring(4, encryptedText.length() - 1);
            }
            
            String decrypted = sm4Encryptor.decrypt(textToDecrypt);
            result.put("success", true);
            result.put("encrypted", encryptedText);
            result.put("decrypted", decrypted);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
    
    /**
     * 生成新的SM4密钥
     */
    @GetMapping("/generate-key")
    public Map<String, Object> generateKey() {
        Map<String, Object> result = new HashMap<>();
        try {
            String keyHex = SM4Util.generateKey();
            result.put("success", true);
            result.put("keyHex", keyHex);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
    
    /**
     * 测试配置解密
     */
    @GetMapping("/test-config")
    public Map<String, Object> testConfig() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("datasourcePassword", configService.getDatasourcePassword());
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "SM4 Encryption Service");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }
    
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}