package com.baoge.sm2_sm4_cbc;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;

public class SM2SM4Example {
    public static void main(String[] args) throws Exception {
        // 1. 生成SM2密钥对
        AsymmetricCipherKeyPair keyPair = SM2KeyGenerator.generateKeyPair();
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();

        System.out.println("公钥: " + Hex.toHexString(publicKey.getQ().getEncoded(false)));
        System.out.println("私钥: " + privateKey.getD().toString(16));
        
        // 2. 生成随机的SM4密钥
        byte[] sm4Key = new byte[16]; // SM4密钥长度16字节(128位)
        new SecureRandom().nextBytes(sm4Key); // 生成一个安全的随机字节数组作为SM4算法的加密密钥
        
        // 3. 使用SM2公钥加密SM4密钥
        byte[] encryptedSm4Key = SM2Util.encrypt(sm4Key, publicKey);
        
        // 4. 使用SM4加密数据
        String originalData = "这是一段需要加密的敏感数据";
        byte[] encryptedData = SM4CBCUtils.encrypt(originalData.getBytes("UTF-8"), sm4Key);
        
        // 传输 encryptedSm4Key 和 encryptedData 给接收方
        
        // 5. 接收方使用SM2私钥解密SM4密钥
        byte[] decryptedSm4Key = SM2Util.decrypt(encryptedSm4Key, privateKey);
        
        // 6. 使用解密后的SM4密钥解密数据
        byte[] decryptedData = SM4CBCUtils.decrypt(encryptedData, decryptedSm4Key);
        String result = new String(decryptedData, "UTF-8");
        
        System.out.println("原始数据: " + originalData);
        System.out.println("解密结果: " + result);
    }
}