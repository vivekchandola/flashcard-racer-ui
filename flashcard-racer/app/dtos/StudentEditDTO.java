package dtos;

import dtos.domain.Login;

public class StudentEditDTO implements Login {

	private String login;
	
	private String password;
	
	private String verifyPassword;
	
	public String validate() {
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
