package com.baoge.config;

import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 高级SM4加密器（支持GCM模式）
 */
@Component("advancedSM4Encryptor")
@ConditionalOnProperty(name = "sm4.encrypt.enable-advanced", havingValue = "true")
public class AdvancedSM4Encryptor implements StringEncryptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AdvancedSM4Encryptor.class);
    
    private static final String ALGORITHM = "SM4";
    private static final String TRANSFORMATION = "SM4/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;
    
    private final String secretKey;
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Autowired
    public AdvancedSM4Encryptor(SM4Properties sm4Properties) {
        this.secretKey = sm4Properties.getKey();
        logger.info("高级SM4加密器初始化完成（GCM模式）");
    }
    
    @Override
    public String encrypt(String message) {
        try {
            if (message == null) return null;
            
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
            
            byte[] encrypted = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            
            // 组合IV和加密数据
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
            
            String result = Base64.getEncoder().encodeToString(combined);
            logger.debug("高级SM4加密成功，原文长度: {}", message.length());
            return result;
        } catch (Exception e) {
            logger.error("高级SM4加密失败", e);
            throw new RuntimeException("高级SM4加密失败", e);
        }
    }
    
    @Override
    public String decrypt(String encryptedMessage) {
        try {
            if (encryptedMessage == null) return null;
            
            byte[] combined = Base64.getDecoder().decode(encryptedMessage);
            
            if (combined.length < IV_LENGTH) {
                throw new IllegalArgumentException("无效的加密数据");
            }
            
            // 分离IV和加密数据
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[combined.length - IV_LENGTH];
            
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encrypted, 0, encrypted.length);
            
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, "BC");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            
            byte[] decrypted = cipher.doFinal(encrypted);
            String result = new String(decrypted, StandardCharsets.UTF_8);
            logger.debug("高级SM4解密成功，密文长度: {}", encryptedMessage.length());
            return result;
        } catch (Exception e) {
            logger.error("高级SM4解密失败", e);
            throw new RuntimeException("高级SM4解密失败", e);
        }
    }
}