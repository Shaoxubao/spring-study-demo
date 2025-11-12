package com.baoge.config;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Jasypt配置类
 */
@Configuration
public class JasyptSM4Config {
    
    @Autowired
    private SM4Properties sm4Properties;
    
    /**
     * 默认使用基础SM4加密器
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "advancedSM4Encryptor")
    @ConditionalOnProperty(name = "sm4.encrypt.enable-advanced", havingValue = "false", matchIfMissing = true)
    public StringEncryptor stringEncryptor() {
        return new SM4StringEncryptor(sm4Properties);
    }
    
    /**
     * 如果启用高级模式，使用高级SM4加密器
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "sm4.encrypt.enable-advanced", havingValue = "true")
    public StringEncryptor advancedStringEncryptor() {
        return new AdvancedSM4Encryptor(sm4Properties);
    }
}