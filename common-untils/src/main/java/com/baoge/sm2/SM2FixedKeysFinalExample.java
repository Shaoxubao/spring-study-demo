package com.baoge.sm2;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.Security;
import java.security.SecureRandom;

public class SM2FixedKeysFinalExample {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 正确的测试密钥对
    private static final String TEST_PUBLIC_KEY = 
        "04d84ffdc916cc8f63e37d8e5534f38d2a4f9b0ce802ce04ae58fb58765c7882c444932277f26c24854d7bb0e912cc6e2ac177aaf7e79e185c80d26cd270388b07";
    
    private static final String TEST_PRIVATE_KEY = 
        "6c5954604458fd0e6cb792050ff61e183cf13823892f0ef01ff318131ef87257";

    public static void main(String[] args) {
        try {
            System.out.println("=== SM2加密解密最终版 ===");
            
            // 1. 创建密钥对象
            ECPublicKeyParameters publicKey = createPublicKeyFromHex(TEST_PUBLIC_KEY);
            ECPrivateKeyParameters privateKey = createPrivateKeyFromHex(TEST_PRIVATE_KEY);

            System.out.println("使用公钥: " + TEST_PUBLIC_KEY);
            System.out.println("使用私钥: " + TEST_PRIVATE_KEY);

            // 2. 加密
            String originalText = "测试SM2加密解密";
            System.out.println("\n原始文本: " + originalText);
            
            byte[] encryptedData = sm2Encrypt(publicKey, originalText.getBytes("UTF-8"));
            System.out.println("加密结果(hex): " + Hex.toHexString(encryptedData));

            // 3. 解密
            byte[] decryptedData = sm2Decrypt(privateKey, encryptedData);
            System.out.println("解密结果: " + new String(decryptedData, "UTF-8"));

        } catch (Exception e) {
            System.err.println("发生错误:");
            e.printStackTrace();
        }
    }

    // 从16进制字符串创建公钥
    private static ECPublicKeyParameters createPublicKeyFromHex(String publicKeyHex) {
        try {
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(
                    sm2ECParameters.getCurve(),
                    sm2ECParameters.getG(),
                    sm2ECParameters.getN(),
                    sm2ECParameters.getH());

            // 验证公钥格式
            if (!publicKeyHex.startsWith("04") || publicKeyHex.length() != 130) {
                throw new IllegalArgumentException("公钥必须以04开头且长度为130字符");
            }

            byte[] publicKeyBytes = Hex.decode(publicKeyHex);
            ECPoint q = sm2ECParameters.getCurve().decodePoint(publicKeyBytes);
            
            if (!q.isValid()) {
                throw new IllegalArgumentException("公钥点不在曲线上");
            }

            return new ECPublicKeyParameters(q, domainParameters);
        } catch (Exception e) {
            throw new RuntimeException("创建公钥失败: " + e.getMessage(), e);
        }
    }

    // 从16进制字符串创建私钥
    private static ECPrivateKeyParameters createPrivateKeyFromHex(String privateKeyHex) {
        try {
            X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
            ECDomainParameters domainParameters = new ECDomainParameters(
                    sm2ECParameters.getCurve(),
                    sm2ECParameters.getG(),
                    sm2ECParameters.getN(),
                    sm2ECParameters.getH());

            BigInteger d = new BigInteger(privateKeyHex, 16);
            
            // 验证私钥范围
            if (d.compareTo(BigInteger.ONE) < 0 || d.compareTo(domainParameters.getN()) >= 0) {
                throw new IllegalArgumentException("私钥不在有效范围内");
            }

            return new ECPrivateKeyParameters(d, domainParameters);
        } catch (Exception e) {
            throw new RuntimeException("创建私钥失败: " + e.getMessage(), e);
        }
    }

    // SM2加密（使用标准模式）
    private static byte[] sm2Encrypt(ECPublicKeyParameters publicKey, byte[] data) {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2); // 使用标准模式
        engine.init(true, new ParametersWithRandom(publicKey, new SecureRandom()));
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (Exception e) {
            throw new RuntimeException("加密失败: " + e.getMessage(), e);
        }
    }

    // SM2解密（使用与加密相同的模式）
    private static byte[] sm2Decrypt(ECPrivateKeyParameters privateKey, byte[] encryptedData) {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2); // 必须与加密模式一致
        engine.init(false, privateKey);
        try {
            return engine.processBlock(encryptedData, 0, encryptedData.length);
        } catch (Exception e) {
            throw new RuntimeException("解密失败: " + e.getMessage(), e);
        }
    }
}