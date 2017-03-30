package actormessages;

import dtos.Card;

public class CardSolved {

	private final String userId;
	private final Card card;

	public CardSolved(String userId, Card card) {
		super();
		this.userId = userId;
		this.card = card;
	}

	public String getUserId() {
		return userId;
	}

	public Card getCard() {
		return card;
	}
}
