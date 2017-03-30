package dtos;

import dtos.domain.Login;
import play.data.validation.Constraints.Required;

public class LoginDTO implements Login{

    @Required
    private String login;
	
    @Required
    private String password;

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

    /**
     * TODO To be implemented
     */
	@Override
	public String getVerifyPassword() {
		return null;
	}

    /**
     * TODO To be implemented
     */
	@Override
	public String validate() {
		return null;
	}
	
}
