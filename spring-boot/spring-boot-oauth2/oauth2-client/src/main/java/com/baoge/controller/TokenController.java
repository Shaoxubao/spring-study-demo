package com.baoge.controller;

import com.baoge.exception.MyException;
import com.baoge.model.ResponseData;
import com.baoge.model.po.User;
import com.baoge.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Value("${auth-call-back-url}")
    private String authCallBackUrl;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.password}")
    private String clientPassword;

    @Value("${third-token-url}")
    private String thirdTokenUrl;

    @Value("${get-third-user-info-url}")
    private String getThirdUserInfoUrl;

    @Autowired
    private UserService userService;

    // 第三方资源服务器返回用户认证成功标识authCode
    @RequestMapping(value = "/callBack", method = RequestMethod.GET)
    public ResponseData callBack(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Basic " + new String(Base64.encodeBase64((clientId + ":" + clientPassword).getBytes())));

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("redirect_uri", authCallBackUrl);

        try {
            String result = new RestTemplate().postForObject(thirdTokenUrl, new HttpEntity<>(params, headers), String.class);
            if (result == null) {
                throw new MyException(ResponseData.STATUS_REQUEST_FAILED, "企业认证失败！");
            }

            JSONObject json = new JSONObject(result);

            return handleThirdUser(json.optString("access_token"), json.optString("refresh_token"));
        } catch (RestClientResponseException e) {
            e.printStackTrace();
        }

        return new ResponseData(ResponseData.STATUS_REQUEST_FAILED, "", "登录失败！");
    }

    private ResponseData handleThirdUser(String accessToken, String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Bearer " + accessToken);

        try {
            String result = new RestTemplate().exchange(getThirdUserInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();

            //将第三方用户写入本系统中
            JSONObject json = new JSONObject(result);
            User user = new User();
            user.setAccount("user_" + System.currentTimeMillis());
            user.setPassword("123456");
            user.setNickName(json.optString("nickName"));
            user.setMobile(json.optString("mobile"));
            user.setThirdAccount(json.optString("account"));
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);

            return new ResponseData(ResponseData.STATUS_OK, userService.addUser(user), "登录成功！");
        } catch (RestClientResponseException e) {
            e.printStackTrace();
        }

        return new ResponseData(ResponseData.STATUS_REQUEST_FAILED, "", "登录失败！");
    }
}