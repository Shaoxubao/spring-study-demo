package com.baoge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 对于复杂的业务，LiteFlow可以把流程进行模块化拆分，使其变为一个个组件；通过定义一个规则来对组件进行编排，
 * 使每个组件可以根据实际需求来进行流转。
 * <p>
 * 快速入门
 * 打个比方，每天下班回到家以后，通常的操作有洗手、吃饭、追剧；
 */

@SpringBootApplication
public class LiteFlowApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiteFlowApplication.class, args);
    }
}
