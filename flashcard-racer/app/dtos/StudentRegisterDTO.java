package dtos;

import dtos.domain.Login;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class StudentRegisterDTO implements Login{

		@Required
		@MinLength(5)
		private String login;
		
		@Required
		private String password;
		
		@Required
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
