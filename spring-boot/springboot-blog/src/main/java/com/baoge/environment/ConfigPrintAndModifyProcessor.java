package com.baoge.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.*;

/**
 * 打印并修改application.yml配置的EnvironmentPostProcessor
 */
public class ConfigPrintAndModifyProcessor implements EnvironmentPostProcessor {

    // 指定要处理的配置文件名称
    private static final String YAML_CONFIG_FILE = "application.yml";
    // 自定义处理后的配置源名称（用于覆盖原有配置）
    private static final String PROCESSED_CONFIG_SOURCE_NAME = "processed-application-config";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            // ========== 第一步：打印application.yml中的所有配置 ==========
            System.out.println("======= 开始打印application.yml原始配置 =======");
            printApplicationYmlConfig(environment);
            System.out.println("======= 结束打印application.yml原始配置 =======\n");

            // ========== 第二步：统一修改配置 ==========
            System.out.println("======= 开始统一修改配置 =======");
//            modifyApplicationConfig(environment);
            System.out.println("======= 配置修改完成 =======\n");

            // ========== 第三步：打印修改后的完整配置（验证效果） ==========
            System.out.println("======= 开始打印修改后的所有配置 =======");
//            printAllConfigSources(environment);
            System.out.println("======= 结束打印修改后的所有配置 =======");

        } catch (Exception e) {
            throw new RuntimeException("处理application.yml配置失败", e);
        }
    }

    /**
     * 精准打印application.yml中的所有配置项
     */
    private void printApplicationYmlConfig(ConfigurableEnvironment environment) throws Exception {
        // 1. 加载classpath下的application.yml文件
        Resource resource = new ClassPathResource(YAML_CONFIG_FILE);
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        // 加载YAML配置（返回多个PropertySource，对应yml中的多环境配置，比如dev/prod）
        List<PropertySource<?>> yamlPropertySources = loader.load(YAML_CONFIG_FILE, resource);

        // 2. 遍历并打印每个配置项
        for (PropertySource<?> propertySource : yamlPropertySources) {
            System.out.println("配置源名称：" + propertySource.getName());
            // 获取配置源的底层Map（存储所有配置键值对）
            Map<String, Object> sourceMap = (Map<String, Object>) propertySource.getSource();
            // 递归解析嵌套的配置（比如app.config.max-retry这种层级结构）
            printNestedConfig(sourceMap, "");
        }
    }

    /**
     * 递归打印嵌套的YAML配置（处理app.config.max-retry这种层级键）
     */
    private void printNestedConfig(Map<String, Object> sourceMap, String parentKey) {
        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = parentKey.isEmpty() ? entry.getKey() : parentKey + "." + entry.getKey();
            Object value = entry.getValue();

            // 如果值是Map，说明是嵌套配置，递归解析
            if (value instanceof Map) {
                printNestedConfig((Map<String, Object>) value, key);
            } else {
                // 打印最终的键值对
                System.out.printf("配置项：%-30s 值：%s%n", key, value);
            }
        }
    }

    /**
     * 统一修改配置（核心逻辑：添加新的PropertySource覆盖原有配置）
     */
    private void modifyApplicationConfig(ConfigurableEnvironment environment) {
        MutablePropertySources propertySources = environment.getPropertySources();
        Properties processedProps = new Properties();

        // 1. 读取原始配置并统一修改
        // 示例1：修改超时时间（将秒改为毫秒，直接覆盖原值）
        String originalTimeout = environment.getProperty("app.config.timeout");
        if (originalTimeout != null) {
            long timeoutMs = Long.parseLong(originalTimeout) * 1000;
            processedProps.setProperty("app.config.timeout", String.valueOf(timeoutMs));
            System.out.println("修改配置：app.config.timeout → 原值=" + originalTimeout + "，新值=" + timeoutMs);
        }

        // 示例2：补全接口地址后缀（如果没有/则添加）
        String originalApiUrl = environment.getProperty("app.config.api-url");
        if (originalApiUrl != null && !originalApiUrl.endsWith("/")) {
            String newApiUrl = originalApiUrl + "/";
            processedProps.setProperty("app.config.api-url", newApiUrl);
            System.out.println("修改配置：app.config.api-url → 原值=" + originalApiUrl + "，新值=" + newApiUrl);
        }

        // 示例3：设置默认值（如果配置未定义则添加）
        String maxRetry = environment.getProperty("app.config.max-retry");
        if (maxRetry == null) {
            processedProps.setProperty("app.config.max-retry", "5");
            System.out.println("添加默认配置：app.config.max-retry = 5");
        }

        // 示例4：批量修改IP列表（拆分字符串为数组格式）
        String originalIps = environment.getProperty("app.config.allowed-ips");
        if (originalIps != null) {
            String newIps = originalIps.replace(",", "|"); // 示例：将,替换为|
            processedProps.setProperty("app.config.allowed-ips", newIps);
            System.out.println("修改配置：app.config.allowed-ips → 原值=" + originalIps + "，新值=" + newIps);
        }

        // 2. 将修改后的配置添加到Environment中（优先级高于原有配置，实现覆盖）
        if (!processedProps.isEmpty()) {
            PropertiesPropertySource processedSource = new PropertiesPropertySource(PROCESSED_CONFIG_SOURCE_NAME, processedProps);
            // 添加到最前面，确保优先使用修改后的配置
            propertySources.addFirst(processedSource);
        }
    }

    /**
     * 打印Environment中所有配置源的配置（验证修改效果）
     */
    private void printAllConfigSources(ConfigurableEnvironment environment) {
        MutablePropertySources propertySources = environment.getPropertySources();
        for (PropertySource<?> source : propertySources) {
            System.out.println("【配置源】：" + source.getName());
            // 跳过系统/环境变量等冗余配置（可选）
            if (source.getName().startsWith("system") || source.getName().startsWith("env")) {
                continue;
            }
            // 打印当前配置源的所有有效配置项
            if (source.getSource() instanceof Map) {
                Map<String, Object> sourceMap = (Map<String, Object>) source.getSource();
                sourceMap.forEach((k, v) -> System.out.printf("  %-30s 值：%s%n", k, v));
            }
        }
    }
}