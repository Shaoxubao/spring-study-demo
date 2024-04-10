package com.baoge.config.auth.model;

import com.baoge.model.Client;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;

import java.util.*;

public class MyClientDetails implements ClientDetails {
    private final Client client;

    public MyClientDetails(Client client) {
        this.client = client;
    }

    @Override
    public String getClientId() {
        return client.getAccount();
    }

    @Override
    public Set<String> getResourceIds() {
        return new HashSet<>();
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return new BCryptPasswordEncoder().encode(client.getPassword());
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        Set<String> set = new HashSet<>();
        set.add("read");

        return set;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        Set<String> set = new HashSet<>();
        set.add("authorization_code");
        set.add("refresh_token");

        return set;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        Set<String> set = new HashSet<>();
        set.add(client.getCallBackUrl());

        return set;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return client.getAccessTokenOverdueSeconds();
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return client.getRefreshTokenOverdueSeconds();
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return new HashMap<>();
    }
}
