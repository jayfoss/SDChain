package com.fintare.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import com.fintare.security.BadKeyException;
import com.fintare.security.CipherOutput;
import com.fintare.security.PEMContent;

public class FileManager {
	public void writeKeys(String fileName, PublicKey publicKey, CipherOutput cipherOutput, byte[] salt) {
		try (FileWriter fw = new FileWriter(fileName)) {
			try (BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write("-----BEGIN EC PUBLIC KEY-----\r\n");
				bw.write(CommonUtils.toBase64(publicKey.getEncoded()));
				bw.write("\r\n-----END EC PUBLIC KEY-----\r\n");
				bw.write("-----BEGIN EC PRIVATE KEY-----\r\n");
				bw.write("Proc-Type: 4,ENCRYPTED\r\n");
				bw.write("DEK-Info: AES-256-CBC," + CommonUtils.toBase64(cipherOutput.getIv()) + ","
						+ CommonUtils.toBase64(salt) + "\r\n");
				bw.write("\r\n");
				bw.write(CommonUtils.toBase64(cipherOutput.getCipherText()));
				bw.write("\r\n-----END EC PRIVATE KEY-----");
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		publicKey = null;
	}

	public PEMContent readKeys(String fileName) throws BadKeyException {
		ArrayList<String> content = new ArrayList<String>(5);
		try (FileReader fr = new FileReader(fileName)) {
			String line = null;
			try(BufferedReader br = new BufferedReader(fr)){
				while ((line = br.readLine()) != null) {
					content.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String publicKeyString = null;
		String privateKeyString = null;
		HashMap<String, String[]> prkHeaders = new HashMap<String, String[]>();
		byte state = -1;
		for(int i = 0; i < content.size() - 1; i++) {
			String line = content.get(i);
			if(line.equals("-----BEGIN EC PUBLIC KEY-----") && state == -1) {
				state = 0;
				continue;
			}
			if(state == 0) {
				publicKeyString = line;
				state = 1;
				continue;
			}
			if(line.equals("-----END EC PUBLIC KEY-----") && state == 1) {
				state = 2;
				continue;
			}
			if(line.equals("-----BEGIN EC PRIVATE KEY-----") && state == 2) {
				state = 3;
				continue;
			}
			if(line.contains(":") && state == 3) {
				String[] header = line.split(":");
				prkHeaders.put(header[0], header[1].split(","));
				continue;
			}
			if(line.trim().equals("") && state == 3) {
				state = 4;
				continue;
			}
			if(state == 4) {
				if(line.trim().equals("")) {
					continue;
				}
				privateKeyString = line;
				state = 5;
				continue;
			}
			if(line.equals("-----END EC PRIVATE KEY-----") && state == 5) {
				state = 6;
				continue;
			}
		}
		if(publicKeyString == null) {
			throw new BadKeyException("Public key not found in key file.");
		}
		if(privateKeyString == null) {
			throw new BadKeyException("Private key not found in key file.");
		}
		return new PEMContent(publicKeyString, privateKeyString, prkHeaders);
	}
}
