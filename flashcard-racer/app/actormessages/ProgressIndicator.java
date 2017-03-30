package actormessages;

public class ProgressIndicator {
	private final String userId;
	private final int percentDone;

	public ProgressIndicator(String userId, int percentDone) {
		super();
		this.userId = userId;
		this.percentDone = percentDone;
	}

	public String getUserId() {
		return userId;
	}

	public int getPercentDone() {
		return percentDone;
	}

}
