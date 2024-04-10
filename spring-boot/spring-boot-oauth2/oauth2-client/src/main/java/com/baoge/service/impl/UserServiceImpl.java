package com.baoge.service.impl;

import com.baoge.exception.MyException;
import com.baoge.mapper.UserMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Value("${get-third-user-info-url}")
    private String getThirdUserInfoUrl;

    @Value("${third-token-url}")
    private String thirdTokenUrl;

    @Value("${auth.client-id}")
    private String clientId;

    @Value("${auth.password}")
    private String clientPassword;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User addUser(User user) {
        String thirdAccount = user.getThirdAccount();
        if (thirdAccount != null && !thirdAccount.equals("")) {
            User findUser = userMapper.findByThirdAccount(thirdAccount);
            if (findUser == null) {
                if (userMapper.insert(user) == 1) {
                    return user;
                }
            } else {
                user.setUserId(findUser.getUserId());

                return updateUser(user);
            }
        }

        return null;
    }

    @Override
    public User findById(int userId) {
        return userMapper.findById(userId);
    }

    @Override
    public User updateUser(User user) {
        User updateUser = findById(user.getUserId());
        if (updateUser == null) {
            throw new MyException(ResponseData.STATUS_REQUEST_FAILED, "用户不存在！");
        }

        if (user.getPassword() != null) {
            updateUser.setPassword(user.getPassword());
        }

        if (user.getNickName() != null) {
            updateUser.setNickName(user.getNickName());
        }

        if (user.getMobile() != null) {
            updateUser.setMobile(user.getMobile());
        }

        if (user.getAccessToken() != null) {
            updateUser.setAccessToken(user.getAccessToken());
        }

        if (user.getRefreshToken() != null) {
            updateUser.setRefreshToken(user.getRefreshToken());
        }

        if (userMapper.update(updateUser) == 1) {
            return updateUser;
        } else {
            throw new MyException(ResponseData.STATUS_REQUEST_FAILED, "更新失败！");
        }
    }

    @Override
    public User validateUser(String account, String password) {
        User findUser = userMapper.findByAccount(account);
        if (findUser != null && findUser.getPassword().equals(password)) {
            return findUser;
        }

        return null;
    }

    @Override
    public User findThirdUser(int userId) {
        User findUser = findById(userId);

        String accessToken = findUser.getAccessToken();
        if (accessToken != null && !accessToken.equals("")) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("authorization", "Bearer " + accessToken);

            try {
                String result = new RestTemplate().exchange(getThirdUserInfoUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class).getBody();

                // 更新用户信息
                JSONObject json = new JSONObject(result);
                findUser.setNickName(json.optString("nickName"));
                findUser.setMobile(json.optString("mobile"));
                findUser.setThirdAccount(json.optString("account"));
                updateUser(findUser);

                return findUser;
            } catch (RestClientResponseException e) {
                if (e.getRawStatusCode() == 401) {
                    String newAccessToken = refreshAccessToken(findUser.getRefreshToken());
                    if (newAccessToken.equals("")) {
                        //refreshToken过期后清空refreshToken和aceessToken
                        findUser.setAccessToken("");
                        findUser.setRefreshToken("");
                        updateUser(findUser);

                        throw new MyException(ResponseData.STATUS_IDENTITY_OVERDUE, "第三方登录已过期，请重新登录！");
                    }

                    //登录未过期，更新accessToken
                    findUser.setAccessToken(newAccessToken);
                    updateUser(findUser);

                    return findThirdUser(userId);
                }
            }
        }

        return null;
    }

    //以refreshToken刷新accessToken
    private String refreshAccessToken(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("authorization", "Basic " + new String(Base64.encodeBase64((clientId + ":" + clientPassword).getBytes())));

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        String accessToken;
        try {
            String result = new RestTemplate().postForObject(thirdTokenUrl, new HttpEntity<>(params, headers), String.class);
            accessToken = new JSONObject(result).optString("access_token");
        } catch (RestClientResponseException e) {
            //400 badRequest
            accessToken = "";
        }

        return accessToken;
    }
}
