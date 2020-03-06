package org.guitar.DAO.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordHashing {

	public static void main(String args[]) {
		
	}

	public static String passwordEncoded(String input) {

		byte[] message = input.getBytes(StandardCharsets.UTF_8);
		String encoded = Base64.getEncoder().encodeToString(message);
		return encoded;
	}
	
	public static String passwordDecoded(String input) {

		byte[] decoded = Base64.getDecoder().decode(input);
		String message = new String(decoded, StandardCharsets.UTF_8);
		return message;
	}
}