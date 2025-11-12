package com.baoge.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;

/**
 * 国密对称加密算法
 * 
 * @author henry
 *
 */
@Slf4j
public class SM4Util {
	
	static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final Charset ENCODING = StandardCharsets.UTF_8;
    
    public static final String ALGORITHM_NAME = "SM4";
    
    // 加密算法/分组加密模式/分组填充方式
    // PKCS5Padding-以8个字节为一组进行分组加密
    // 定义分组加密模式使用：PKCS5Padding
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    
    // 128-32位16进制；256-64位16进制
    public static final int DEFAULT_KEY_SIZE = 128;
    
    public static final String DEFAULT_KEY = "86C63180C2806ED1F47B859DE501215B";
    public static final String DEFAULT_IV = "8F5CB6272B594B53AD1A2197361378DC";
    
    /**
     * 生成ECB暗号
     * @explain ECB模式（电子密码本模式：Electronic codebook）
     * @param algorithmName
     *            算法名称
     * @param mode
     *            模式
     * @param key
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws InvalidKeyException
     */
    private static Cipher generateCipherECB(String algorithmName, int mode, byte[] key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, new SecretKeySpec(key, ALGORITHM_NAME));
        return cipher;
    }
    
    /**
     * 生成CBC暗号
     * @param algorithmName
     *            算法名称
     * @param mode
     *            模式
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    private static Cipher generateCipherCBC(String algorithmName, int mode, byte[] key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        cipher.init(mode, new SecretKeySpec(key, ALGORITHM_NAME), new IvParameterSpec(iv));
        return cipher;
    }

    /**
     * 自动生成密钥
     * @explain
     * @return
     */
    public static String generateKey() {
        try {
			return HexUtil.byteToHex(generateKey(DEFAULT_KEY_SIZE));
		} catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            log.error("=======generateKey-error:[key is null]===========");
		}
        return null;
    }

    /**
     * @explain
     * @param keySize
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    public static byte[] generateKey(int keySize) throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM_NAME, BouncyCastleProvider.PROVIDER_NAME);
        kg.init(keySize, new SecureRandom());
        return kg.generateKey().getEncoded();
    }

    /**
     * 加密模式之Ecb
     * @explain
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    protected static byte[] encryptECBPadding(byte[] data, byte[] key) throws Exception {
        Cipher cipher = generateCipherECB(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }
    
	/**
	 * 解密
	 * @explain
	 * @param key
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	protected static byte[] decryptECBPadding(byte[] encrypted, byte[] key) {
        byte[] buff = new byte[0];
        Cipher cipher = null;
        try {
            cipher = generateCipherECB(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
            buff = cipher.doFinal(encrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                NoSuchProviderException | InvalidKeyException e) {
            log.error("=============decryptECBPadding-cipher-error=============");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            log.error("=============decryptECBPadding-cipher.doFinal-error=============");
        }
        return buff;
    }

    /**
     * 加密模式之cbc
     * @explain
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    protected static byte[] encryptCBCPadding(byte[] data, byte[] key, byte[] iv) {
        byte[] buff = new byte[0];
        try {
            Cipher cipher = generateCipherCBC(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key, iv);
            buff = cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                NoSuchProviderException | InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            log.error("=============encryptCBCPadding-cipher-error=============");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            log.error("=============encryptCBCPadding-cipher.doFinal-error=============");
        }
        return buff;
    }
    
	/**
	 * 解密
	 * @explain
	 * @param key
	 * @param encrypted
	 * @return
	 * @throws Exception
	 */
	protected static byte[] decryptCBCPadding(byte[] encrypted, byte[] key, byte[] iv) {
	    byte[] buff = new byte[0];
        try {
            Cipher cipher = generateCipherCBC(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key, iv);
            buff = cipher.doFinal(encrypted);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException |
                NoSuchProviderException | InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            log.error("=============decryptCBCPadding-cipher-error=============");
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            log.error("=============decryptCBCPadding-cipher.doFinal-error=============");
        }
        return buff;
    }
    
	/**
     * 加密
     * @explain 加密模式：ECB
     * @param source
     *            待加密字符串
     * @return 返回16进制的加密字符串
     */
    public static String encrypt(String source) {
        return encrypt(source, DEFAULT_KEY);
    }
    
    
	/**
     * 加密
     * @explain 加密模式：ECB
     * @param source
     *            待加密数据
     * @return 返回16进制的加密字符串
     */
    public static String encrypt(byte[] source) {
        return encrypt(source, DEFAULT_KEY);
    }
    
	/**
     * 加密
     * @explain 加密模式：ECB
     * @param source
     *            待加密字符串
     * @param hexKey
     *            16进制密钥
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encrypt(String source, String hexKey) {
        return encrypt(source.getBytes(ENCODING), hexKey);
    }

    /**
     * 加密
     * @explain 加密模式：ECB
     * @param source
     *            待加密字符串
     * @param hexKey
     *            16进制密钥
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encrypt(byte[] source, String hexKey) {
        byte[] cipherArray;
		try {
			cipherArray = encryptECBPadding(source, ByteUtils.fromHexString(hexKey));
			return ByteUtils.toHexString(cipherArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return null;
    }

    
    /**
     * 解密
     * @explain 解密模式：采用ECB
     * @param encrypted
     *            16进制的加密字符串
     * @return 解密后的字符串
     */
    public static byte[] decrypt(String encrypted) {
		return decrypt(encrypted, DEFAULT_KEY);
    }
    
    /**
     * 解密
     * @explain 解密模式：采用ECB
     * @param encrypted
     *            16进制的加密字符串
     * @return 解密后的字符串
     */
    public static String decryptToString(String encrypted) {
		return decryptToString(encrypted, DEFAULT_KEY);
    }
    
    /**
     * 解密
     * @explain 解密模式：采用ECB
     * @param encrypted
     *            16进制的加密字符串
     * @param hexKey
     *            16进制密钥
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptToString(String encrypted, String hexKey) {
		return new String(decrypt(encrypted, hexKey), ENCODING);
    }
    
    /**
     * 解密
     * @explain 解密模式：采用ECB
     * @param encrypted
     *            16进制的加密字符串
     * @param hexKey
     *            16进制密钥
     * @return 
     * @throws Exception
     */
    public static byte[] decrypt(String encrypted, String hexKey) {
        byte[] decryptBytes = decryptECBPadding(
                ByteUtils.fromHexString(encrypted), ByteUtils.fromHexString(hexKey));
		if(decryptBytes.length == 0){
            decryptBytes = encrypted.getBytes(StandardCharsets.UTF_8);
        }
		return decryptBytes;
    }
    
    /**
     * 加密
     * @explain 加密模式：CBC
     * @param source
     *            待加密字符串
     * @param hexKey
     *            16进制密钥
     * @param iv
     *            16进制偏移量
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encrypt(String source, String hexKey, String iv) {
        return encrypt(source.getBytes(ENCODING), hexKey, iv);
    }
    
    /**
     * 加密
     * @explain 加密模式：CBC
     * @param source
     *            待加密字符串
     * @param hexKey
     *            16进制密钥
     * @param iv
     *            16进制偏移量
     * @return 返回16进制的加密字符串
     * @throws Exception
     */
    public static String encrypt(byte[] source, String hexKey, String iv) {
        byte[] cipherArray = encryptCBCPadding(
                source
                ,ByteUtils.fromHexString(hexKey)
                , ByteUtils.fromHexString(iv));
        if(cipherArray.length == 0){
            log.error("数据加密失败");
        }
        return ByteUtils.toHexString(cipherArray);
    }
    
    /**
     * 解密
     * @explain 解密模式：采用CBC
     * @param encrypted
     *            16进制的加密字符串
     * @param hexKey
     *            16进制密钥
     * @param iv
     *            16进制偏移量
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decryptToString(String encrypted, String hexKey, String iv) {
		return new String(decrypt(encrypted, hexKey, iv), ENCODING);
    }

    /**
     * 解密
     * @explain 解密模式：采用CBC
     * @param encrypted
     *            16进制的加密字符串
     * @param hexKey
     *            16进制密钥
     * @param iv
     *            16进制偏移量
     * @return 解密后的字符串
     * @throws Exception
     */
    public static byte[] decrypt(String encrypted, String hexKey, String iv) {
        if (StringUtils.isBlank(encrypted)) {
            return new byte[] {};
        }
        return decryptCBCPadding(
                ByteUtils.fromHexString(encrypted)
                ,ByteUtils.fromHexString(hexKey)
                , ByteUtils.fromHexString(iv));
    }

    /**
     * 解密请求数据并转成json
     *
     * @param request
     * @return
     */
    public static String decryptRequestToJson(String request) throws UnsupportedEncodingException {
        String jsonString = "";
        if(StringUtils.isNotBlank(request)){
            // Base64密文串
            StringBuilder ecryptBuilder = new StringBuilder();
            // 获取密文数组
            String[] ecryptArray = request.split("\\|");
            // 解密密文
            for (String ecrypt : ecryptArray) {
                ecryptBuilder.append(decryptToString(ecrypt));
            }
            // 获取base64字符串
            String base64 = StringUtils.toEncodedString(
                    Base64Utils.decodeFromString(ecryptBuilder.toString()), Charset.defaultCharset());
            // Base64转json串
            jsonString = URLDecoder.decode(base64, Charset.defaultCharset().name());
        }
        return jsonString;
    }

    public static String decryptRequestToJson(String request, String key, String iv) throws UnsupportedEncodingException {
        String jsonString = "";
        if(StringUtils.isNotBlank(request)){
            // Base64密文串
            StringBuilder ecryptBuilder = new StringBuilder();
            // 获取密文数组
            String[] ecryptArray = request.split("\\|");
            // 解密密文
            for (String ecrypt : ecryptArray) {
                ecryptBuilder.append(decryptToString(ecrypt, key, iv));
            }
            // 获取base64字符串
            String base64 = StringUtils.toEncodedString(
                    Base64Utils.decodeFromString(ecryptBuilder.toString()), Charset.defaultCharset());
            // Base64转json串
            jsonString = URLDecoder.decode(base64, Charset.defaultCharset().name());
        }
        return jsonString;
    }

    public static void main(String[] args) {
        String key = "92b8fcd846c0462ab7c4f770cc580589";
        String iv = "5b045f15649e48cb960bbab7ed8587c2";

//        String str = "1234567890123456";
//        String encrypt = encrypt(str, key, iv);
//        System.out.println("加密后：" + encrypt);
//        String decrypt = decryptToString(encrypt, key, iv);
//        System.out.println("解密后：" + decrypt);

        String str = "123456";
        String encrypt = encrypt(str);
        System.out.println("加密后：" + encrypt);
        String decrypt = decryptToString(encrypt);
        System.out.println("解密后：" + decrypt);

    }

}
