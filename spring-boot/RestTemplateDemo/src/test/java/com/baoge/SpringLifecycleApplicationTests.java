package com.baoge;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoge.config.RestTemplateConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest(classes = RestTemplateDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
class SpringLifecycleApplicationTests {

    @Test
    void contextLoads() {
        log.info("contextLoads is running");
    }

    @Test
    void testRestTemplateDemo() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String url = "https://192.168.43.8:10921/auth/getToken";
        RestTemplate restTemplateHttps = new RestTemplate(RestTemplateConfig.generateHttpRequestFactory());
        // 方式一
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
        paramMap.add("account", "wangxing");
        paramMap.add("password", "123456");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        // headers可以详细设置请求头中的信息等
        // HttpEntity里面包含了请求方和相应方的请求头和请求体,类似于@RequestBody和@ResponseBody

        // 方式二：设置请求参数为JSON格式
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", "wangxing");
        jsonObject.put("password", "123456");
//        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(paramMap, headers);
        // 创建请求实体
        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString(), headers);
        ResponseEntity<String> results = restTemplateHttps.exchange(url, HttpMethod.POST, httpEntity, String.class);
        JSONObject json = JSON.parseObject(results.getBody());
        System.out.println(json);
    }

}
