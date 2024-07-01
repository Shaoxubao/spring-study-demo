package com.baoge;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MockServerTest {
    @Test
    public void testMockGet() {
        HttpResponse response = HttpRequest.get("http://127.0.0.1:10080/hello")
                .timeout(10000) // 超时，毫秒
                .execute();
        log.info("test response : {}", response.toString());
        log.info("test response body : {}", response.body());
    }

    @Test
    public void testMockPost() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "ESP_SYS205099");
        jsonObject.put("password", "0438c1c25b3c46d904698c58fc5f38e1aa6c3c5fb8d140f5b3f4f8792e380110a479f88a7ff859245409869d7f1f121fbb8eee416afbe1afd34d1d4563e62021843f46a38a33bdf29f64510364015d6ba6c323d97aaaf3a93b91887ac2097870f6a9025de80457eb23");
        HttpResponse response = HttpRequest.post("http://127.0.0.1:10080" + "/ami/ms01-00-605/sys-auth/sysLogin/v1")
                .header(Header.CONTENT_TYPE, "application/json")
                .body(jsonObject.toJSONString())
                .timeout(10000) // 超时，毫秒
                .execute();

        log.info("test response : {}", response.toString());
        log.info("test response body : {}", response.body());
    }
}
