package com.baoge.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class HexUtil extends Hex {
	/**
	 * 整形转换成网络传输的字节流（字节数组）型数据
	 * 
	 * @param num
	 *            一个整型数据
	 * @return 4个字节的自己数组
	 */
	public static byte[] intToBytes(int num) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (0xff & (num >> 0));
		bytes[1] = (byte) (0xff & (num >> 8));
		bytes[2] = (byte) (0xff & (num >> 16));
		bytes[3] = (byte) (0xff & (num >> 24));
		return bytes;
	}

	/**
	 * 四个字节的字节数据转换成一个整形数据
	 * 
	 * @param bytes
	 *            4个字节的字节数组
	 * @return 一个整型数据
	 */
	public static int byteToInt(byte[] bytes) {
		int num = 0;
		int temp;
		temp = (0x000000ff & (bytes[0])) << 0;
		num = num | temp;
		temp = (0x000000ff & (bytes[1])) << 8;
		num = num | temp;
		temp = (0x000000ff & (bytes[2])) << 16;
		num = num | temp;
		temp = (0x000000ff & (bytes[3])) << 24;
		num = num | temp;
		return num;
	}

	/**
	 * 长整形转换成网络传输的字节流（字节数组）型数据
	 * 
	 * @param num
	 *            一个长整型数据
	 * @return 4个字节的自己数组
	 */
	public static byte[] longToBytes(long num) {
		byte[] bytes = new byte[8];
		for (int i = 0; i < 8; i++) {
			bytes[i] = (byte) (0xff & (num >> (i * 8)));
		}

		return bytes;
	}

	/**
	 * 大数字转换字节流（字节数组）型数据
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] byteConvert32Bytes(BigInteger n) {
		if (n == null) {
			return null;
		}

		byte[] bytes = null;
		if (n.toByteArray().length == 33) {
			bytes = new byte[32];
			System.arraycopy(n.toByteArray(), 1, bytes, 0, 32);
		} 
		else if (n.toByteArray().length == 32) {
			bytes = n.toByteArray();
		} 
		else {
			bytes = new byte[32];
			for (int i = 0; i < 32 - n.toByteArray().length; i++) {
				bytes[i] = 0;
			}
			System.arraycopy(n.toByteArray(), 0, bytes, 32 - n.toByteArray().length, n.toByteArray().length);
		}
		
		return bytes;
	}

	/**
	 * 换字节流（字节数组）型数据转大数字
	 * 
	 * @param b
	 * @return
	 */
	public static BigInteger byteConvertInteger(byte[] b) {
		if (b[0] < 0) {
			byte[] temp = new byte[b.length + 1];
			temp[0] = 0;
			System.arraycopy(b, 0, temp, 1, b.length);
			return new BigInteger(temp);
		}
		return new BigInteger(b);
	}

	/**
	 * 根据字节数组获得值(十六进制数字)
	 * 
	 * @param bytes
	 * @param upperCase
	 * @return
	 */
	public static String byteToHex(byte[] bytes, boolean upperCase) {
		if (bytes == null) {
			return null;
		}
		
		String hex = encodeHexString(bytes);
		return upperCase ? hex.toUpperCase() : hex.toLowerCase();
	}

	/**
	 * 字节数组转换为十六进制字符串
	 * 
	 * @param b
	 *            byte[] 需要转换的字节数组
	 * @return String 十六进制字符串
	 */
	public static String byteToHex(byte[] b) {
		return byteToHex(b, true);
	}

	/**
	 * Convert hex string to byte[]
	 * 
	 * @param hex
	 *            the hex string
	 * @return byte[]
	 */
	public static byte[] hexToByte(String hex) {
		try {
			return decodeHex(hex);
		} catch (DecoderException e) {
			return null;
		}
	}
}
