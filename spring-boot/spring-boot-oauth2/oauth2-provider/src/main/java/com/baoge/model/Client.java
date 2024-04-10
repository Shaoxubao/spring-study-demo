package com.baoge.model;

public class Client {
    private int clientId;
    private String clientName;
    private String account;
    private String password;
    private String callBackUrl;
    private int accessTokenOverdueSeconds;
    private int refreshTokenOverdueSeconds;

    public Client() {

    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public int getAccessTokenOverdueSeconds() {
        return accessTokenOverdueSeconds;
    }

    public void setAccessTokenOverdueSeconds(int accessTokenOverdueSeconds) {
        this.accessTokenOverdueSeconds = accessTokenOverdueSeconds;
    }

    public int getRefreshTokenOverdueSeconds() {
        return refreshTokenOverdueSeconds;
    }

    public void setRefreshTokenOverdueSeconds(int refreshTokenOverdueSeconds) {
        this.refreshTokenOverdueSeconds = refreshTokenOverdueSeconds;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId=" + clientId +
                ", clientName='" + clientName + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", callBackUrl='" + callBackUrl + '\'' +
                ", accessTokenOverdueSeconds=" + accessTokenOverdueSeconds +
                ", refreshTokenOverdueSeconds=" + refreshTokenOverdueSeconds +
                '}';
    }
}
