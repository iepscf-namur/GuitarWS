package org.guitar.DAO.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class PasswordHashing {

	// Used to generate random SALT
	private static Random rand = new Random((new Date()).getTime());

	public static void main(String args[]) {
	}

	public static String salting(String input) {
		// https://examples.javacodegeeks.com/core-java/security/encrypt-decrypt-with-salt/
		// When salting, we will basically construct a string with a created salt + the
		// given string input ans Base64 the whole thing
		StringBuilder saltToString = new StringBuilder();
		// BASE64Encoder encoder = new BASE64Encoder();

		byte[] salt = new byte[24];
		// Fills the elements of a specified array of bytes with random numbers.
		rand.nextBytes(salt);
		// I want my salt to be a String and not a byte array for DB storage purpose
		char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		for (int idx = 0; idx < salt.length; idx++) {
			byte b = salt[idx];
			saltToString.append(digits[(b & 0xf0) >> 4]);
			saltToString.append(digits[b & 0x0f]);
		}

		// Will encode salt + input string in Base64
		// return encoder.encode(saltToString.toString().getBytes()) +
		// encoder.encode(input.getBytes());

		String mySalt = saltToString.toString();

		System.out.println("MON SEL EST" + mySalt);
		return Base64.getEncoder().withoutPadding().encodeToString(mySalt.getBytes(StandardCharsets.UTF_8))
				+ Base64.getEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
	}

	public static String unSalting(String saltedInput) {
		// We will get only the Unbased64 part without the salt
		// BASE64Decoder decoder = new BASE64Decoder();
		String saltedInputDecoded = "";

		// saltedInputDecoded = new String(decoder.decodeBuffer(saltedInput));

		byte[] decoded = Base64.getDecoder().decode(saltedInput);
		saltedInputDecoded = new String(decoded, StandardCharsets.UTF_8);

		if (saltedInputDecoded.length() > 48) {
			String cipher = saltedInputDecoded.substring(48);
			return cipher;
		}
		return null;
	}

	public static String getSalt(String saltedInput) {
		// We will get only the Unbased64 salt
		// BASE64Decoder decoder = new BASE64Decoder();
		String saltedInputDecoded = "";

		// saltedInputDecoded = new String(decoder.decodeBuffer(saltedInput));

		byte[] decoded = Base64.getDecoder().decode(saltedInput);
		saltedInputDecoded = new String(decoded, StandardCharsets.UTF_8);

		if (saltedInputDecoded.length() > 48) {
			String cipher = saltedInputDecoded.substring(0, 48);

			return cipher;
		}
		return null;
	}

	public static String generateHash(String input) {
		// https://dzone.com/articles/storing-passwords-java-web
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");

			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

			for (int idx = 0; idx < hashedBytes.length; idx++) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// Handle error here
		}
		return hash.toString();
	}
	
}
