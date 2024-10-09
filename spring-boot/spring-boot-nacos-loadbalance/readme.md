                                        Sping Cloud Nacos 负载均衡
    一、使用OpenFeign
    OpenFeign的内部使用 Ribbon 进行负载均衡（Feign 内置了 Ribbon, OpenFeign在Feign的基础上支持了SpringMVC的注解），
    所以实际上是修改Ribbon的负载均衡策略配置。
    1、使用OpenFeign时有两种实现方式：
        a.全局服务策略配置
        全局范围 - 该服务消费者调用的所有服务通用策略，在要配置服务的 xxxApplication 类中定义一个新的 IRule
        // 如果需要配置成其他的负载均衡，返回值改成对应的规则类。
        @Bean
        public IRule randomRule() {
            return new RandomRule();
        }
        b.指定服务策略配置
        “调用方服务” 配置文件需要增加 “被调用方服务” 的负载均衡配置
        例如：服务spring-client-a 业务中需要调用 spring-client-b项目接口， spring-client-b为多实例， 
        调用过程中需要负载均衡， 那么spring-client-a项目yaml配置文件增加如下配置：
        # 服务名称，配置根节点（与spring同级） (集群配置，避免重复配置，可以根据具体情况将负载均衡策略配置在Nacos共享配置文件中)
        spring-client-b:
          ribbon:
            NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule #配置规则 随机
            #    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RoundRobinRule #配置规则 轮询
            #    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RetryRule #配置规则 重试
            #    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.WeightedResponseTimeRule #配置规则 响应时间权重
            #    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.BestAvailableRule #配置规则 最空闲连接策略
            ConnectTimeout: 500 #请求连接超时时间
            ReadTimeout: 1000 #请求处理的超时时间
            OkToRetryOnAllOperations: true #对所有请求都进行重试
            MaxAutoRetriesNextServer: 2 #切换实例的重试次数
            MaxAutoRetries: 1 #对当前实例的重试次数
    二、使用注解方式：在消费者启动类代码中注入RestTemplate，@LoadBalanced的作用是开启负载均衡，默认是轮询机制。
    @SpringBootApplication
    @EnableDiscoveryClient
    public class Consumer80Application {
        public static void main(String[] args) {
            SpringApplication.run(Consumer80Application.class, args);
        }
        //注入ribbon调用类RestTemplate
        @Bean
        @LoadBalanced
        RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }