package util;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import models.Student;

public class StudentAuthenticator {

	public static boolean authenticate(String username, String password) throws NoSuchAlgorithmException {
		Student s = Student.db().find(Student.class).where().eq("name", username).findUnique();

		if (s != null) {
			byte[] salt = s.getSalt();
			byte[] hash = s.getHash();
			byte[] inputHash = hashString(salt, password);

			return Arrays.equals(hash, inputHash);
		}
		
		return false;

	}

	protected static byte[] hashString(byte[] salt, String string) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		CharBuffer buffer = CharBuffer.wrap(string.toCharArray());
		byte[] hashable = Charset.forName("UTF-8").encode(buffer).array();

		digest.update(salt);
		digest.update(hashable);
		return digest.digest();
	}

}
