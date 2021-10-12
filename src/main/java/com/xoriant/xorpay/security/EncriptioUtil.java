package com.xoriant.xorpay.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncriptioUtil {

	private static final String AES = "AES";
	private static final String AES_CBC_PKCS5PADDING = "AES/CBC/PKCS5PADDING";
	private static final String UTF_8 = "UTF-8";
	private static final String INIT_VECTOT = "Ahe43Djj354!@#qW";
	private static final String SEC_KEY = "AheEH%j354!*#qWk";

	public static String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOT.getBytes(UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(SEC_KEY.getBytes(StandardCharsets.UTF_8), AES);

			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			byte[] original = Base64.getEncoder().encode(encrypted);
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(INIT_VECTOT.getBytes(UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(SEC_KEY.getBytes(StandardCharsets.UTF_8), AES);

			Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5PADDING);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
