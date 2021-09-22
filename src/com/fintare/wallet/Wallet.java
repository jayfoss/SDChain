package com.fintare.wallet;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.fintare.security.BadKeyException;
import com.fintare.security.CipherOutput;
import com.fintare.security.PEMContent;
import com.fintare.security.SecurityHelper;
import com.fintare.transaction.Transaction;
import com.fintare.transaction.TransactionData;
import com.fintare.transaction.TransactionSignatureException;
import com.fintare.util.CommonUtils;
import com.fintare.util.FileManager;

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public Wallet() {
		this.generateKeys();
	}

	public Wallet(String fileName, String password) {
		SecurityHelper s = new SecurityHelper();
		try {
			PEMContent pem = new FileManager().readKeys(fileName);
			this.publicKey = s.getPublicKey(pem.getPublicKey());
			String[] dekInfo = pem.getPrivateKeyHeaders().get("DEK-Info");
			SecretKey sk = s.deriveKey(password, CommonUtils.fromBase64(dekInfo[2]));
			byte[] iv = CommonUtils.fromBase64(dekInfo[1]);
			this.privateKey = s.getPrivateKey(pem.getPrivateKey(), sk, iv);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException
				| BadKeyException e) {
			e.printStackTrace();
		}
		password = null;
		System.gc();
	}

	private void generateKeys() {
		SecurityHelper s = new SecurityHelper();
		try {
			KeyPair keys = s.generateAsymmetricKeys();
			this.privateKey = keys.getPrivate();
			this.publicKey = keys.getPublic();
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	public void saveKeys(String password, String fileName) {
		FileManager f = new FileManager();
		SecurityHelper s = new SecurityHelper();
		try {
			byte[] salt = s.getSalt();
			SecretKey key = s.deriveKey(password, salt);
			CipherOutput c = s.encrypt(privateKey.getEncoded(), key);
			f.writeKeys(fileName, publicKey, c, salt);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException
				| InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		f = null;
		password = null;
		System.gc();
	}

	public String getPublicKey() {
		return CommonUtils.toBase64(new X509EncodedKeySpec(publicKey.getEncoded()).getEncoded());
	}
	
	public Transaction createTransaction(TransactionData data, boolean from) throws TransactionSignatureException {
		String json = data.getJSON();
		byte[] dataBytes = json.getBytes(StandardCharsets.UTF_8);
		SecurityHelper s = new SecurityHelper();
		try {
			byte[] signature = s.sign(dataBytes, privateKey);
			String signatureHex = CommonUtils.toHex(signature);
			Transaction t;
			if(from) {
				t = new Transaction(data, signatureHex, null);
			}
			else {
				t = new Transaction(data, null, signatureHex);
			}
			return t;
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
			throw new TransactionSignatureException("Transaction could not be signed and was not created.");
		}
	}
	
	public Transaction createTransaction(Transaction trans, boolean from) throws TransactionSignatureException {
		if(trans.getToSignature() == null && from) {
			throw new TransactionSignatureException("Transaction cannot be co-signed by sender if recipient has not signed it.");
		}
		if(trans.getFromSignature() == null && !from) {
			throw new TransactionSignatureException("Transaction cannot be co-signed by recipient if sender has not signed it.");
		}
		String json = trans.getData().getJSON();
		byte[] dataBytes = json.getBytes(StandardCharsets.UTF_8);
		SecurityHelper s = new SecurityHelper();
		try {
			byte[] signature = s.sign(dataBytes, privateKey);
			String signatureHex = CommonUtils.toHex(signature);
			Transaction t;
			if(from) {
				t = new Transaction(trans.getData(), signatureHex, trans.getToSignature());
			}
			else {
				t = new Transaction(trans.getData(), trans.getFromSignature(), signatureHex);
			}
			return t;
		} catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
			throw new TransactionSignatureException("Transaction could not be co-signed and was not updated.");
		}
	}
}
