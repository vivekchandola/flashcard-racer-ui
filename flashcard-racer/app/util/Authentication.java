package util;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Student;
import models.Teacher;

public final class Authentication {

	private static final int usernameMinLength = 5;
	private static final int passwordMinLength = 5;

	private static final int usernameMaxLength = 50;
	private static final int passwordMaxLength = 50;
 
	private static final String usernameDescription =
		"Usernames must consist of at least 5 characters. " + 
		"Allowed characters are a-z, A-Z, and the characters _ and -.";
	private static final String passwordDescription =
		"Passwords must consist of at least 5 characters. " +
		"Allowed characters are any non whitespace character.";

	private static final String usernameRegex = 
		"^[a-zA-Z0-9_-]{" + usernameMinLength + "," + usernameMaxLength + "}$";
	private static final String passwordRegex = 
		"^\\S{" + passwordMinLength + "," + passwordMaxLength + "}$";

	private static final Pattern usernamePattern = Pattern.compile(usernameRegex); 
	private static final Pattern passwordPattern = Pattern.compile(passwordRegex);
	

	public static final int getUsernameMinLength() {
		return usernameMinLength;
	}
	public static final int getUsernameMaxLength() {
		return usernameMaxLength;
	}
	public static final int getPasswordMinLength() {
		return passwordMinLength;
	}
	public static final int getPasswordMaxLength() {
		return passwordMaxLength;
	}
	public static final String getUsernameDescription() {
		return usernameDescription;
	}
	public static final String getPasswordDescription() {
		return passwordDescription;
	}
	public static boolean validateUsername(String username) {
		Matcher usernameMatcher = usernamePattern.matcher(username);
		return usernameMatcher.matches();
	}
	public static boolean validatePassword(String password) {
		Matcher passwordMatcher = usernamePattern.matcher(password);
		return passwordMatcher.matches();
	}

	


	public static boolean authenticateTeacher(String username, String password) 
		throws NoSuchAlgorithmException {
		Teacher teacher = Teacher.db().find(Teacher.class)
			.where().eq("name", username).findUnique();

		if (teacher != null) {
			byte[] salt = teacher.getSalt();
			byte[] hash = teacher.getHash();
			byte[] inputHash = hashString(salt, password);

			if (Arrays.equals(hash, inputHash)) {
				return true;
			}
		}
		return false;
	}

	public static boolean authenticateStudent(String username, String password)		
			throws NoSuchAlgorithmException  {
		Student s = Student.db().find(Student.class)
				.where().eq("name", username).findUnique();

			if (s != null) {
				byte[] salt = s.getSalt();
				byte[] hash = s.getHash();
				byte[] inputHash = hashString(salt, password);

				if (Arrays.equals(hash, inputHash)) {
					return true;
				}
			}
			return false;		
		
	}

	public static boolean authenticateAdmin(String username, String password) {
		return username.equals("admin") && password.equals("admin");
	}

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
