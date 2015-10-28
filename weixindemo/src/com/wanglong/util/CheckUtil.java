package com.wanglong.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CheckUtil {

	// 开发者自己写的token
	public static final String TOKEN = "wanglong";

	/**
	 *  验证signature
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return ture or false
	 */
	public static boolean checkSignature(String signature, String timestamp,
			String nonce) {

		// 1 将参数组装成数组
		String[] arr = new String[] { TOKEN, timestamp, nonce };

		// 2 排序
		Arrays.sort(arr);

		// 生产字符串,为了跟服务器传入的signature比较
		StringBuffer content = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		// 3 进行sha1加密
		String str = SHA1(content.toString());
		
		//4 返回比较后的值
		return str.equals(signature);

	}

	/**
	 * SHA1加密
	 * @param decript
	 * @return
	 */
	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

}
