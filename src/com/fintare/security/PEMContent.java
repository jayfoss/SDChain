package com.fintare.security;

import java.util.HashMap;

public class PEMContent {
	private String publicKey;
	private String privateKey;
	private HashMap<String, String[]> privateKeyHeaders;
	
	public PEMContent(String publicKey, String privateKey, HashMap<String, String[]> privateKeyHeaders) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.privateKeyHeaders = privateKeyHeaders;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public HashMap<String, String[]> getPrivateKeyHeaders() {
		return privateKeyHeaders;
	}
	
	
}
