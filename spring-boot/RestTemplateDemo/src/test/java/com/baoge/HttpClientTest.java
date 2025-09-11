package com.baoge;

import com.alibaba.fastjson.JSONObject;
import com.baoge.bean.DeclareDataVo;
import com.baoge.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void testHttpClient3() {
        log.info("test http client");

        String url = "https://127.0.0.1:10921/api/upDeclareData";

        DeclareDataVo declareDataVo = new DeclareDataVo();

        declareDataVo.setPlantDeclareId("123");
        declareDataVo.setPlantId("123");
        declareDataVo.setPlantNo("123");
        declareDataVo.setPlantName("123");
        declareDataVo.setPlantType("123");
        declareDataVo.setRespType("123");
        declareDataVo.setStartTime("123");
        declareDataVo.setEndTime("123");
        declareDataVo.setContinueTime("123");
        declareDataVo.setExecuteTime("123");
        declareDataVo.setDeclareTime("123");
        declareDataVo.setMarketType(1);
        declareDataVo.setPackageType(1);

        String declareDataVoJson = JSONObject.toJSONString(declareDataVo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data", declareDataVoJson);

        Map<String,String> heads = new HashMap<>();
        heads.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ2aXJ0dWFscG93ZXIiLCJleHAiOjE3NTc0MTg3MTYsInZpcnR1YWxwb3dlciI6InRlc3QiLCJpYXQiOjE3NTc0MTE1MTZ9.xEmg0lj7Ru0oBePv7QMVlbEDtgS3aGZiUJ8wYX1YWYM");
        String r = HttpClientUtils.doHttpsPostJson(url, jsonObject.toString(), heads);
        log.info("r:{}",r);
    }
}
