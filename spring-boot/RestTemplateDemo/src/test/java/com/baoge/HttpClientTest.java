package com.baoge;

import com.alibaba.fastjson.JSONObject;
import com.baoge.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = RestTemplateDemoApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class HttpClientTest {
    @Test
    public void testHttpClient() {
        log.info("test http client");

        String url = "https://127.0.0.1:10921/auth/getToken";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("account", "test");
        jsonObject.put("password", "123456");
        String r = HttpClientUtils.doHttpsPostJson(url, jsonObject.toString(), null);
        log.info("r:{}",r);
    }

    @Test
    public void testHttpClient2() {
        log.info("test http client");

        String url = "http://127.0.0.1:29999/api/getDeclareData";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("declareDate", "2023-12-27");
        jsonObject.put("plantName", "西安挥合坤能源科技有限公司");
        jsonObject.put("plantNo", "FW0001");
        String r = HttpClientUtils.doHttpsPostJson(url, jsonObject.toString(), null);
        log.info("r:{}",r);
    }
}
