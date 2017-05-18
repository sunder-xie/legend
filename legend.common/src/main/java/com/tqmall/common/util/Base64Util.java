package com.tqmall.common.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

/**
 * base64加、解密工具类
 * 
 * @Author 王佳超<jiachao.wang@tqmall.com>
 * @Create 2015年6月2日上午11:56:50
 */
public class Base64Util {
	/**
	 * base64解码
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		if (StringUtils.isEmpty(str)) {
			throw new RuntimeException("origin could  not be null or empty!");
		}
		Base64 base64 = new Base64();
		byte[] res = base64.encode(str.getBytes());
		return new String(res);
	}
	
	/**
	 * base64解码
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
	    if (StringUtils.isEmpty(str)) {
	      throw new RuntimeException("encodeStr could  not be null or empty!");
	    }
	    Base64 base64 = new Base64();
	    byte[] res = base64.decode(str.getBytes());
	    return new String(res);
	}
}
