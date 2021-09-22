package com.fintare.security;

import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.fintare.util.CommonUtils;

public class SecurityHelper {
	public SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 500000, 256);
		SecretKey key = skf.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(key.getEncoded(), "AES");
		return secret;
	}

	public byte[] getSalt() throws NoSuchAlgorithmException {
		return this.getSalt(32);
	}

	public byte[] getSalt(int length) throws NoSuchAlgorithmException {
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[length];
		rand.nextBytes(salt);
		return salt;
	}

	public KeyPair generateAsymmetricKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
		SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
		ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
		keyGen.initialize(ecSpec, rand);
		KeyPair keys = keyGen.generateKeyPair();
		return keys;
	}

	public CipherOutput encrypt(byte[] plaintext, SecretKey key)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters params = cipher.getParameters();
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		byte[] cipherText = cipher.doFinal(plaintext);
		CipherOutput c = new CipherOutput(cipherText, iv);
		return c;
	}

	public byte[] decrypt(byte[] cipherText, SecretKey key, byte[] iv)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		return cipher.doFinal(cipherText);
	}

	public PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec spec = new X509EncodedKeySpec(CommonUtils.fromBase64(publicKey));
		KeyFactory kf = KeyFactory.getInstance("EC");
		return kf.generatePublic(spec);
	}

	public PrivateKey getPrivateKey(String privateKey, SecretKey key, byte[] iv)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] pk = this.decrypt(CommonUtils.fromBase64(privateKey), key, iv);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pk);
		KeyFactory kf = KeyFactory.getInstance("EC");
		return kf.generatePrivate(spec);
	}

	public byte[] sign(byte[] data, PrivateKey privateKey)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("EC");
		sig.initSign(privateKey);
		sig.update(data);
		return sig.sign();
	}
	
	public boolean verify(byte[] data, PublicKey publicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		Signature sig = Signature.getInstance("EC");
		sig.initVerify(publicKey);
		sig.update(data);
		return sig.verify(signature);
	}

	public byte[] sha256(String str) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
		return hash;
	}

}
