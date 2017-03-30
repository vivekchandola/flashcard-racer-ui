package actormessages;

public class NextCard {

	private final String userId;
	
	public NextCard(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}
}
