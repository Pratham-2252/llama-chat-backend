package com.prathamesh.app.service;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

	private static final String SECRET_KEY = "12345678901234567890123456789012";
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

	public String decryptData(String encryptedData) throws Exception {

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedData);

		SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);

		Cipher cipher = Cipher.getInstance(TRANSFORMATION);

		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes);
	}
}
