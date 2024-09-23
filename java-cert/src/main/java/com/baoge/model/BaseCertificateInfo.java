package com.baoge.model;

import lombok.Data;

@Data
public class BaseCertificateInfo {

    private String userName;
    /**
     * 1.生成密匙对,存储KeyStore
     * 2.生成CSR证书请求文件
     * 3.生成CER证书
     */
    /**
     * 默认生成密钥算法
     */
    private  String keyAlgorithm = "RSA";
    /**
     * 默认生成密钥长度
     */
    private int keySize = 2048;
    private int vaildDay = 365;
    private String country = "CN";
    private String stateOrProvince = "Shaanxi";
    private String locality = " xi'an";
    private String organization = "nari";
    private String organizationalUnit ="dev";
    private String commonName = userName;
    private String emailAddress ="123@aa.com";
}
