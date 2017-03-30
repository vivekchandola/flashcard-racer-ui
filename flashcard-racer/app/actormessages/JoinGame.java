package actormessages;

public class JoinGame {
	private final String pin;
	private final String userId;
	
	public JoinGame(String pin, String userId) {
		super();
		this.pin = pin;
		this.userId = userId;
	}

	public String getPin() {
		return pin;
	}

	public String getUserId() {
		return userId;
	}
}
