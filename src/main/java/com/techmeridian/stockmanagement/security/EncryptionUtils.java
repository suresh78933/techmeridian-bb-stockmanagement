package com.techmeridian.stockmanagement.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class EncryptionUtils {

	private final static String getSecurePassword(String passwordToHash, byte[] salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	// Add salt
	private final static byte[] getSalt() throws NoSuchAlgorithmException {
		/*
		 * SecureRandom sr = SecureRandom.getInstance("SHA1PRNG"); byte[] salt =
		 * new byte[16]; sr.nextBytes(salt);
		 */

		// TODO: We can change this to take from persistence later, AruN
		String salt = "(1(2(3StockManagementSalt4)5)6){TechMeridian}";
		return salt.getBytes();
	}

	public static String getSecurePassword(String passwordToHash) throws Exception {
		return getSecurePassword(passwordToHash, getSalt());
	}

	private EncryptionUtils() {
	}

	private static EncryptionUtils encryptionUtils;

	public static EncryptionUtils getInstance() {
		if (encryptionUtils == null) {
			encryptionUtils = new EncryptionUtils();
		}
		return encryptionUtils;
	}

	@Override
	public Object clone() {
		return encryptionUtils;
	}
}