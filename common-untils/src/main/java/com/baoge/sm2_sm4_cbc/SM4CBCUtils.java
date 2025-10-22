package com.baoge.sm2_sm4_cbc;

import org.bouncycastle.crypto.engines.SM4Engine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import java.security.SecureRandom;

public class SM4CBCUtils {
    /**
     * 解密SM4-CBC加密数据
     * @param encryptedData 加密后的字节数组（包含IV）
     * @param key 解密密钥
     * @return 解密后的字节数组
     * @throws Exception 解密过程中可能抛出的异常
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] key) throws Exception {
        // 提取IV
        byte[] iv = new byte[16];
        System.arraycopy(encryptedData, 0, iv, 0, iv.length);

        // 提取实际密文
        byte[] cipherText = new byte[encryptedData.length - iv.length];
        System.arraycopy(encryptedData, iv.length, cipherText, 0, cipherText.length);

        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new SM4Engine()));
        cipher.init(false, new ParametersWithIV(new KeyParameter(key), iv));

        byte[] output = new byte[cipher.getOutputSize(cipherText.length)];
        int len = cipher.processBytes(cipherText, 0, cipherText.length, output, 0);
        len += cipher.doFinal(output, len);

        // 去除可能的填充
        byte[] result = new byte[len];
        System.arraycopy(output, 0, result, 0, len);

        return result;
    }

    /**
     * 加密SM4-CBC数据
     * @param data 待加密的字节数组
     * @param key 加密密钥
     * @return 加密后的字节数组（包含IV）
     * @throws Exception 加密过程中可能抛出的异常
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成随机IV
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        PaddedBufferedBlockCipher cipher = new PaddedBufferedBlockCipher(
                new CBCBlockCipher(new SM4Engine()));
        cipher.init(true, new ParametersWithIV(new KeyParameter(key), iv));

        byte[] output = new byte[cipher.getOutputSize(data.length)];
        int len = cipher.processBytes(data, 0, data.length, output, 0);
        len += cipher.doFinal(output, len);

        // 将IV和密文合并返回
        byte[] result = new byte[iv.length + len];
        System.arraycopy(iv, 0, result, 0, iv.length);
        System.arraycopy(output, 0, result, iv.length, len);

        return result;
    }
}
