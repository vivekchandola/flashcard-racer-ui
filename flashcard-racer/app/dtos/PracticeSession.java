package dtos;

import models.enums.Difficulty;
import util.CardUtil;

public class PracticeSession {

	private int sessionLength;
	private int current;
	private int numCorrect;
	private int numIncorrect;
	private int timer;
	private Difficulty difficulty;
	private Card card;

	public PracticeSession() {
		super();
	}

	public int getSessionLength() {
		return sessionLength;
	}

	public void setSessionLength(int sessionLength) {
		this.sessionLength = sessionLength;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public int getNumCorrect() {
		return numCorrect;
	}

	public void setNumCorrect(int numCorrect) {
		this.numCorrect = numCorrect;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public static PracticeSession startSession(Difficulty diff) {
		PracticeSession session = new PracticeSession();

		session.setDifficulty(diff);
		session.setSessionLength(diff.getNumber());
		session.setCurrent(0);
		session.setNumCorrect(0);
		session.setNumIncorrect(0);
		session.setTimer(diff.getTimer());
		session.setCard(CardUtil.randomCard(diff));

		return session;
	}

	public static void advanceSession(PracticeSession session) {
		session.current++;

		if (CardUtil.evaluate(session.card)) {
			session.numCorrect += 1;
		} else {
			session.numIncorrect += 1;
		}

		session.card = CardUtil.randomCard(session.difficulty);
	}

	@Override
	public String toString() {
		return "PracticeSession [sessionLength=" + sessionLength + ", current=" + current + ", numCorrect=" + numCorrect
				+ ", numIncorrect=" + numIncorrect + ", difficulty=" + difficulty + ", timer=" + timer + ", card="
				+ card + "]";
	}

	public int getTimer() {
		return timer;
	}

	public void setTimer(int timer) {
		this.timer = timer;
	}

	public int getNumIncorrect() {
		return numIncorrect;
	}

	public void setNumIncorrect(int numIncorrect) {
		this.numIncorrect = numIncorrect;
	}

}
