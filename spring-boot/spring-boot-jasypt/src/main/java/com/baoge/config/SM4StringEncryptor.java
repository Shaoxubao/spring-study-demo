package com.baoge.config;

import com.baoge.utils.SM4Util;
import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基础SM4加密器实现
 */
@Component("sm4StringEncryptor")
public class SM4StringEncryptor implements StringEncryptor {
    
    private static final Logger logger = LoggerFactory.getLogger(SM4StringEncryptor.class);
    
    private final SM4Properties sm4Properties;
    
    @Autowired
    public SM4StringEncryptor(SM4Properties sm4Properties) {
        this.sm4Properties = sm4Properties;
    }
    
    @Override
    public String encrypt(String message) {
        if (message == null) {
            return null;
        }
        
        try {
            String encrypted = SM4Util.encrypt(message);
            logger.debug("SM4加密成功，原文长度: {}", message.length());
            return encrypted;
        } catch (Exception e) {
            logger.error("SM4加密失败", e);
            throw new RuntimeException("SM4加密失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String decrypt(String encryptedMessage) {
        if (encryptedMessage == null) {
            return null;
        }
        
        try {
            String decrypted = SM4Util.decryptToString(encryptedMessage);
            logger.info("SM4解密成功，decrypted: {}", decrypted);
            return decrypted;
        } catch (Exception e) {
            logger.error("SM4解密失败", e);
            throw new RuntimeException("SM4解密失败: " + e.getMessage(), e);
        }
    }
}