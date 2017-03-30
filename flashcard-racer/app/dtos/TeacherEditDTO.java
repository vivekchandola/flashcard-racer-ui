package dtos;

import dtos.domain.Login;
import play.data.validation.Constraints.MinLength;
import util.Authentication;

public class TeacherEditDTO implements Login{

	@MinLength(5)
	private String login;

	private String password;

	private String verifyPassword;

	public String validate() {
		if (!login.equals("") && !Authentication.validateUsername(login)) {
			return Authentication.getUsernameDescription();
		} 
		else if (!password.equals("") && !Authentication.validatePassword(password)) {
			return Authentication.getPasswordDescription();
		}
		else if (!password.equals(verifyPassword)) {
			return "Passwords do not match!";
		}
		else if (login.equals("") && password.equals("")) {
			return "No changes to submit. Try the back button.";
		}
		return null;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getVerifyPassword() {
		return verifyPassword;
	}
	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}
}
