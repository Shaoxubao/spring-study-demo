package com.baoge.sm2;

import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.Security;
import java.security.SecureRandom;

public class SM2Example {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static void main(String[] args) throws Exception {
        // 1. 生成密钥对
        AsymmetricCipherKeyPair keyPair = generateKeyPair();
        ECPublicKeyParameters pubKey = (ECPublicKeyParameters) keyPair.getPublic();
        ECPrivateKeyParameters priKey = (ECPrivateKeyParameters) keyPair.getPrivate();

        System.out.println("公钥: " + Hex.toHexString(pubKey.getQ().getEncoded(false)));
        System.out.println("私钥: " + priKey.getD().toString(16));

        // 2. 加密示例
        String plainText = "Hello, SM2!";
        byte[] cipherText = encrypt(pubKey, plainText.getBytes());
        System.out.println("加密结果: " + Hex.toHexString(cipherText));

        // 3. 解密示例
        byte[] decryptedText = decrypt(priKey, cipherText);
        System.out.println("解密结果: " + new String(decryptedText));

        // 4. 签名示例
        byte[] signature = sign(priKey, plainText.getBytes());
        System.out.println("签名结果: " + Hex.toHexString(signature));

        // 5. 验签示例
        boolean verifyResult = verify(pubKey, plainText.getBytes(), signature);
        System.out.println("验签结果: " + verifyResult);
    }

    // 生成SM2密钥对
    public static AsymmetricCipherKeyPair generateKeyPair() {
        // 获取SM2椭圆曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(
                sm2ECParameters.getCurve(),
                sm2ECParameters.getG(),
                sm2ECParameters.getN(),
                sm2ECParameters.getH());

        // 生成密钥对
        ECKeyPairGenerator keyPairGenerator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenerationParameters = new ECKeyGenerationParameters(domainParameters, new SecureRandom());
        keyPairGenerator.init(keyGenerationParameters);
        return keyPairGenerator.generateKeyPair();
    }

    // SM2加密
    public static byte[] encrypt(ECPublicKeyParameters pubKey, byte[] data) {
        SM2Engine engine = new SM2Engine();
        ParametersWithRandom pwr = new ParametersWithRandom(pubKey, new SecureRandom());
        engine.init(true, pwr);
        try {
            return engine.processBlock(data, 0, data.length);
        } catch (Exception e) {
            throw new RuntimeException("SM2加密失败", e);
        }
    }

    // SM2解密
    public static byte[] decrypt(ECPrivateKeyParameters priKey, byte[] cipherText) {
        SM2Engine engine = new SM2Engine();
        engine.init(false, priKey);
        try {
            return engine.processBlock(cipherText, 0, cipherText.length);
        } catch (Exception e) {
            throw new RuntimeException("SM2解密失败", e);
        }
    }

    // SM2签名
    public static byte[] sign(ECPrivateKeyParameters priKey, byte[] data) throws CryptoException {
        org.bouncycastle.crypto.signers.SM2Signer signer = new org.bouncycastle.crypto.signers.SM2Signer();
        signer.init(true, new ParametersWithRandom(priKey, new SecureRandom()));
        signer.update(data, 0, data.length);
        return signer.generateSignature();
    }

    // SM2验签
    public static boolean verify(ECPublicKeyParameters pubKey, byte[] data, byte[] signature) {
        org.bouncycastle.crypto.signers.SM2Signer signer = new org.bouncycastle.crypto.signers.SM2Signer();
        signer.init(false, pubKey);
        signer.update(data, 0, data.length);
        return signer.verifySignature(signature);
    }
}