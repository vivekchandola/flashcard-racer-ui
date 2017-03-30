package actormessages;

public class GetGame {

	private final String pin;

	public GetGame(String pin) {
		super();
		this.pin = pin;
	}

	public String getPin() {
		return pin;
	}
}
