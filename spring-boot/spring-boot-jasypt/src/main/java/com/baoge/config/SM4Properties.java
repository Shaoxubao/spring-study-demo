package com.baoge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sm4.encrypt")
public class SM4Properties {
    
    private String key = "1234567890123456";
    private String algorithm = "SM4/ECB/PKCS5Padding";
    private boolean enableAdvanced = false;
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getAlgorithm() {
        return algorithm;
    }
    
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    
    public boolean isEnableAdvanced() {
        return enableAdvanced;
    }
    
    public void setEnableAdvanced(boolean enableAdvanced) {
        this.enableAdvanced = enableAdvanced;
    }
}