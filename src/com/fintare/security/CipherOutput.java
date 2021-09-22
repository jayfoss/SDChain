package com.fintare.security;

public class CipherOutput {
	private byte[] cipherText;
	private byte[] iv;

	public CipherOutput(byte[] cipherText, byte[] iv) {
		this.cipherText = cipherText;
		this.iv = iv;
	}

	public byte[] getCipherText() {
		return cipherText;
	}

	public byte[] getIv() {
		return iv;
	}
}