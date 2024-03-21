package com.baoge;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baoge.bean.Microservice;
import com.baoge.bean.MicroserviceInstancesResponse;
import com.baoge.bean.MicroservicesResponse;
import com.baoge.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

@Slf4j
public class HutoolHttpTest {

    public static final String CSE_URL = "http://192.168.43.146:30100/v4/default";

    public static final String CSE_TOKEN_URL = "http://192.168.43.146:30100";

    @Test
    public void testHttp() {
        HttpResponse response = HttpRequest.get("http://192.168.43.8:9111/test")
                .timeout(10000) // 超时，毫秒
                .execute();
        log.info("test response : {}", response.toString());
        log.info("test response body : {}", response.body());
    }

    /**
     * 参考ServiceCenterClient类实现
     * 查询所有微服务
     * http://127.0.0.1:30100/v4/default/registry/microservices
     */
    @Test
    public void microservices() throws Exception {
        HttpResponse response = HttpRequest.get(CSE_URL + "/registry/microservices")
                .header("x-domain-name", "default")
                .timeout(10000) // 超时，毫秒
                .execute();
        log.info("test response body : {}", JSON.toJSON(response.body()));
        if (response.getStatus() == HttpStatus.SC_OK) {
            MicroservicesResponse microservicesResponse = HttpUtils.deserialize(response.body(), MicroservicesResponse.class);
            System.out.println(microservicesResponse);

            for (Microservice service : microservicesResponse.getServices()) {
                System.out.println("serviceName: " + service.getServiceName() + ", serviceId: " + service.getServiceId());
            }
        }
    }

    /**
     * 根据serviceId（serviceId由上面接口查得）查询所有微服务实例
     * http://127.0.0.1:30100/v4/default/registry/microservices/{service_id}/instances
     */
    @Test
    public void instances() throws Exception {
        String serviceId = "580ccccd8846a6984a39617aa2ab70de3f10d102";
        HttpResponse response = HttpRequest.get(CSE_URL + "/registry/microservices/" + serviceId + "/instances")
                .header("x-domain-name", "default")
                .timeout(10000) // 超时，毫秒
                .execute();
        log.info("test response body : {}", JSON.toJSON(response.body()));
        if (response.getStatus() == HttpStatus.SC_OK) {
            MicroserviceInstancesResponse instancesResponse = HttpUtils.deserialize(response.body(), MicroserviceInstancesResponse.class);
            System.out.println(instancesResponse);
        }
    }

    @Test
    public void token() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "xubao");
        jsonObject.put("password", "123456");
        HttpResponse response = HttpRequest.post(CSE_TOKEN_URL + "/v4/token")
                .header(Header.CONTENT_TYPE, "application/json")
                .body(jsonObject.toJSONString())
                .timeout(10000) // 超时，毫秒
                .execute();
        System.out.println(response.body());
    }

    @Test
    public void token2() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "xubao");
        jsonObject.put("password", "123456");
        String url = CSE_TOKEN_URL + "/v4/token";
        System.out.println(HttpUtil
                .createPost(url)
                .body(jsonObject.toJSONString())
                .execute()
                .body() );

    }
}
