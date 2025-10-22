package com.baoge.sm2_sm4_cbc;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.security.SecureRandom;

public class SM2Util {

    /**
     * 解密SM2加密数据
     * @param encryptedData 加密数据
     * @param privateKey 私钥
     * @return 解密后的数据
     * @throws InvalidCipherTextException 解密异常
     */
    public static byte[] decrypt(byte[] encryptedData, ECPrivateKeyParameters privateKey) throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        engine.init(false, privateKey);

        return engine.processBlock(encryptedData, 0, encryptedData.length);
    }

    /**
     * 加密SM2数据
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @throws InvalidCipherTextException 加密异常
     */
    public static byte[] encrypt(byte[] data, ECPublicKeyParameters publicKey) throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine();
        engine.init(true, new ParametersWithRandom(publicKey, new SecureRandom()));

        return engine.processBlock(data, 0, data.length);
    }
}
