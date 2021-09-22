package com.fintare.util;

import java.util.Base64;

public class CommonUtils {

	public final static char[] HEX = "0123456789abcdef".toCharArray();

	public static String toBase64(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes);
	}

	public static byte[] fromBase64(String base64) {
		return Base64.getDecoder().decode(base64);
	}

	public static byte[] fromBase64(byte[] base64) {
		return Base64.getDecoder().decode(base64);
	}

	public static String toHex(byte[] bytes) {
		char[] out = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			out[i * 2] = HEX[v >>> 4];
			out[i * 2 + 1] = HEX[v & 0x0F];
		}
		return new String(out);
	}
}