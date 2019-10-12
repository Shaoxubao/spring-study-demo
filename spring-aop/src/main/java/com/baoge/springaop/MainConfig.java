package com.baoge.springaop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @Author shaoxubao
 * @Date 2019/10/12 15:59
 */

@Configuration
@EnableAspectJAutoProxy
public class MainConfig {

    // 将业务逻辑组件和切面类都加入到容器中；告诉Spring哪个是切面类（@Aspect）需要交给spring管理
    @Bean
    public Machine machine() {
        return new Machine();
    }

    @Bean
    public LogAdvice logAdvice() {
        return new LogAdvice();
    }

}
