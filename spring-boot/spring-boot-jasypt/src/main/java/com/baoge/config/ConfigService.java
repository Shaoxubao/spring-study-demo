package com.baoge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * 配置服务，演示如何获取加密的配置值
 */
@Service
public class ConfigService {
    
    @Value("${spring.datasource.password}")
    private String datasourcePassword;
    
    @Autowired
    private Environment environment;
    
    /**
     * 获取解密的数据库密码（自动解密）
     */
    public String getDatasourcePassword() {
        return datasourcePassword; // 自动解密
    }
    
    /**
     * 手动获取加密配置并解密
     */
    public String getManualDecrypt(String propertyName) {
        String encryptedValue = environment.getProperty(propertyName);
        if (encryptedValue != null && encryptedValue.startsWith("ENC(") && encryptedValue.endsWith(")")) {
            // Jasypt会自动解密，这里只是演示
            return encryptedValue;
        }
        return encryptedValue;
    }
}