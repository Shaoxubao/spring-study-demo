package com.baoge.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RedisConfig {

    /**
     * 设置redis服务器的host或者ip地址
     */
    @Value("${universe.redis.hostName}")
    private String hostName;

    /**
     * 设置redis的服务的端口号
     */
    @Value("${universe.redis.port}")
    private Integer port;

    /**
     * 设置密码
     */
    @Value("${universe.redis.password}")
    private String password;

    /**
     * 设置默认使用的数据库
     */
    @Value("${universe.redis.database}")
    private Integer database;

    /**
     * 资源池中的最大连接数
     */
    @Value("${universe.redis.pool.maxTotal}")
    private Integer maxTotal;

    /**
     * 资源池允许的最大空闲连接数
     */
    @Value("${universe.redis.pool.maxIdle}")
    private Integer maxIdle;

    /**
     * 资源池确保的最少空闲连接数
     */
    @Value("${universe.redis.pool.minIdle}")
    private Integer minIdle;

    /**
     * 当资源池用尽后，调用者是否要等待。只有当值为true时，
     * 下面的maxWaitMillis才会生效。
     */
    @Value("${universe.redis.pool.blockWhenExhausted}")
    private Boolean blockWhenExhausted;

    /**
     * 当资源池连接用尽后，调用者的最大等待时间（单位为毫秒）。
     */
    @Value("${universe.redis.pool.maxWaitMillis}")
    private Long maxWaitMillis;

    /**
     * 向资源池借用连接时是否做连接有效性检测（ping）。
     * 检测到的无效连接将会被移除。
     */
    @Value("${universe.redis.pool.testOnBorrow}")
    private Boolean testOnBorrow;

    /**
     * 向资源池归还连接时是否做连接有效性检测（ping）。
     * 检测到无效连接将会被移除。
     */
    @Value("${universe.redis.pool.testOnReturn}")
    private Boolean testOnReturn;

    /**
     * 是否开启JMX监控
     */
    @Value("${universe.redis.pool.jmxEnabled}")
    private Boolean jmxEnabled;

    /**
     * 是否在空闲资源监测时通过ping命令监测连接有效性，
     * 无效连接将被销毁。
     */
    @Value("${universe.redis.pool.testWhileIdle}")
    private Boolean testWhileIdle;

    /**
     * 空闲资源的检测周期（单位为毫秒）
     */
    @Value("${universe.redis.pool.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;

    /**
     * 资源池中资源的最小空闲时间（单位为毫秒），达到此值后空闲资源将被移除。
     */
    @Value("${universe.redis.pool.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;

    /**
     * 做空闲资源检测时，每次检测资源的个数。
     */
    @Value("${universe.redis.pool.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

}
