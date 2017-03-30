package dtos.domain;

public interface Login {

	/**
	 * 
	 * @return username
	 */
	String getLogin();
	
	/**
	 * 
	 * @return password
	 */
	String getPassword();
	
	/**
	 * 
	 * @return verifyPassword
	 */
	String getVerifyPassword();
	
	/**
	 * 
	 * @return boolean
	 */
	String validate();
}
