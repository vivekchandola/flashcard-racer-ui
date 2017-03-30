package util;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HasherSalter {
	
	public static byte[] newSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[256];
		random.nextBytes(salt);
		return salt;
	}

	public static byte[] hashString(byte[] salt, String string) 
		throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		CharBuffer buffer = CharBuffer.wrap(string.toCharArray());
		byte[] hashable = Charset.forName("UTF-8").encode(buffer).array();

		digest.update(salt);
		digest.update(hashable);
		return digest.digest();
	}
}
