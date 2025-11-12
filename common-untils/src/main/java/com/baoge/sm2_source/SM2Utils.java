package com.baoge.sm2_source;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class SM2Utils {
    // 静态初始化：注册BouncyCastle加密提供者
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 生成SM2密钥对（公钥、私钥，16进制字符串）
    public static Map<String, String> generateKeyPair() throws Exception {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(
                sm2ECParameters.getCurve(),
                sm2ECParameters.getG(),
                sm2ECParameters.getN()
        );
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        keyPairGenerator.init(new ECKeyGenerationParameters(domainParameters, new SecureRandom()));
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        // 私钥转16进制
        ECPrivateKeyParameters privateKey = (ECPrivateKeyParameters) keyPair.getPrivate();
        String privateKeyHex = Hex.toHexString(privateKey.getD().toByteArray());

        // 公钥转16进制（压缩格式）
        ECPublicKeyParameters publicKey = (ECPublicKeyParameters) keyPair.getPublic();
        String publicKeyHex = Hex.toHexString(publicKey.getQ().getEncoded(false));

        Map<String, String> keyMap = new HashMap<>();
        keyMap.put("publicKey", publicKeyHex);
        keyMap.put("privateKey", privateKeyHex);
        return keyMap;
    }

    // SM2曲线参数（国密标准曲线sm2p256v1）
    private static final X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    private static final ECDomainParameters ecDomainParameters = new ECDomainParameters(
            x9ECParameters.getCurve(),
            x9ECParameters.getG(), // 生成元
            x9ECParameters.getN()  // 阶
    );


    /**
     * SM2公钥加密
     * @param publicKeyHex 公钥（16进制字符串，支持带04前缀的非压缩格式）
     * @param plainText 明文（UTF-8字符串）
     * @return 加密后密文（16进制字符串，格式：C1C3C2）
     */
    public static String encrypt(String publicKeyHex, String plainText) throws InvalidCipherTextException {
        // 1. 解析公钥（去掉04前缀，转换为ECPoint）
        byte[] publicKeyBytes = Hex.decode(publicKeyHex);
        // 非压缩公钥前2字节为04，直接截取后续内容
//        if (publicKeyBytes.length == 65 && publicKeyBytes[0] == 0x04) {
//            byte[] keyBytes = new byte[64];
//            System.arraycopy(publicKeyBytes, 1, keyBytes, 0, 64);
//            publicKeyBytes = keyBytes;
//        }
        ECPoint publicKeyPoint = x9ECParameters.getCurve().decodePoint(publicKeyBytes);

        // 2. 构建公钥参数
        ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(publicKeyPoint, ecDomainParameters);

        // 3. 初始化SM2加密引擎（模式：C1C3C2，国密推荐）
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        CipherParameters params = new ParametersWithRandom(publicKeyParams, new SecureRandom());
        sm2Engine.init(true, params); // true表示加密模式

        // 4. 执行加密（明文转字节数组，加密后转16进制）
        byte[] plainTextBytes = plainText.getBytes();
        byte[] cipherTextBytes = sm2Engine.processBlock(plainTextBytes, 0, plainTextBytes.length);
        return Hex.toHexString(cipherTextBytes);
    }


    /**
     * SM2私钥解密
     * @param privateKeyHex 私钥（16进制字符串）
     * @param cipherTextHex 密文（16进制字符串，格式：C1C3C2）
     * @return 解密后明文（UTF-8字符串）
     */
    public static String decrypt(String privateKeyHex, String cipherTextHex) throws InvalidCipherTextException {
        // 校验密文是否为合法16进制
        if (!cipherTextHex.matches("^[0-9a-fA-F]+$")) {
            throw new IllegalArgumentException("密文格式错误，必须为16进制字符串");
        }
        // 校验密文长度（至少192个字符）
        if (cipherTextHex.length() < 192) {
            throw new IllegalArgumentException("密文长度不足，可能被截断");
        }
        // C1占64字节（128个16进制字符），补04后C1变为65字节（130个16进制字符）
        String fixedCipherTextHex;
        if (cipherTextHex.length() >= 128) { // 确保C1部分存在
            // 在密文开头插入04（补全C1的标准格式）
            fixedCipherTextHex = "04" + cipherTextHex;
        } else {
            throw new IllegalArgumentException("密文长度不足，格式错误");
        }

        // 1. 解析私钥（转换为BigInteger）
        byte[] privateKeyBytes = Hex.decode(privateKeyHex);
        ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(
                new java.math.BigInteger(1, privateKeyBytes), // 私钥数值
                ecDomainParameters
        );

        // 2. 初始化SM2解密引擎（模式：C1C3C2，需与加密模式一致）
        SM2Engine sm2Engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        sm2Engine.init(false, privateKeyParams); // false表示解密模式

        // 3. 执行解密（密文转字节数组，解密后转字符串）
        byte[] cipherTextBytes = Hex.decode(fixedCipherTextHex);
//        byte[] cipherTextBytes = Hex.decode(cipherTextHex);

        byte[] plainTextBytes = sm2Engine.processBlock(cipherTextBytes, 0, cipherTextBytes.length);
        return new String(plainTextBytes);
    }


    // 测试方法
    public static void main(String[] args) throws Exception {
        Map<String, String> keyPair = generateKeyPair();
        // 测试密钥对（实际应通过generateKeyPair()生成）
        String publicKeyHex = keyPair.get("publicKey");
        String privateKeyHex = keyPair.get("privateKey");
        System.out.println("公钥：" + publicKeyHex);
        System.out.println("私钥：" + privateKeyHex);

        // 待加密明文（例如SM4的key:iv）
        String plainText = "1234567890abcdef1234567890abcdef:fedcba0987654321fedcba0987654321";

        // 加密
//        String cipherText = encrypt(publicKeyHex, plainText);
//        System.out.println("加密后密文：" + cipherText);

        // 解密
//        String decryptedText = decrypt(privateKeyHex, cipherText);
//        System.out.println("解密后明文：" + decryptedText); // 应与plainText一致

//        String pubKey = "04027aec527c2a0af914861ba0df2e9c5b6deddb4060c12f8a71f21a8ae43a274400f90c691ae996e56f0e2138eb87c743e376e5061f5ae9b520c25fcaf85ccdae";
//        String encryptData = encrypt(pubKey, "aaaaa");
//        System.out.println("加密后密文encryptData：" + encryptData);

        String priKey = "636c2ebb0f18213c7c22f78cce342ae25c2e80717fe5ebde54c7838d490eb03a";
        String decryptedFromQianduan = decrypt(priKey, "e8ae2a405856db28bfd9197acc06766fb208b27f1a54e49abeb9033350c662b42cd268f8bdb2b3216d432549ba86926309e45a1a771ec5f11f4fff887f1094a89225b24313bd33fc11bcbb2fced40e4416cd7b6ae1fc3734f35d598634f26d74538f90ced962");
        System.out.println("解密后明文decryptedFromQianduan：" + decryptedFromQianduan);
    }

}