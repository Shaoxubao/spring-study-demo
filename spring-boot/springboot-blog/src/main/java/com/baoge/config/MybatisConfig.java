package com.baoge.config;

/**
 * @author
 * @version 1.0
 * @date 2022/4/13
 * @desc:
 */
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer(){
        return configuration -> configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }


}
