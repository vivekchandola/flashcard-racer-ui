package dtos;

import java.util.ArrayList;
import java.util.List;

import models.enums.Difficulty;
import util.CardUtil;

public class NewGameSession {

	private List<Card> listCards = new ArrayList<>();
	
	private List<String> listOps = new ArrayList<>();
	
	private int timer;
	
	private Double randomNumber;
	
	public NewGameSession createCardDecks(Difficulty diff) {
		NewGameSession decks = new NewGameSession();

		for (int i = 0; i < diff.getNumber(); i++) {
			setListCard(CardUtil.randomCard(diff));
		}
		decks.setListCards(getListCards());
		decks.setTimer(diff.getTimer());
		decks.setListOps(diff.getListOps());
		return decks;
	}
	
	public NewGameSession createCardDecks(UserSelectedChoice userSelectedChoice) {
		NewGameSession decks = new NewGameSession();

		for (int i = 0; i < userSelectedChoice.getNumberOfQuestions(); i++) {
			setListCard(CardUtil.randomCard(userSelectedChoice));
		}
		decks.setListCards(getListCards());
		decks.setTimer(userSelectedChoice.getTimer());
		decks.setListOps(userSelectedChoice.getOperators());
		return decks;
	}


	@Override
	public String toString() {
		return "PracticeSession [listCards=" + listCards + "]";
	}

	public List<Card> getListCards() {
		return listCards;
	}

	public void setListCards(List<Card> listCards) {
		this.listCards = listCards;
	}
	
	public void setListCard(Card card) {
		this.listCards.add(card);
	}

	public Double getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(Double randomNumber) {
		this.randomNumber = randomNumber;
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public List<String> getListOps() {
		return listOps;
	}

	public void setListOps(List<String> listOps) {
		this.listOps = listOps;
	}



}
