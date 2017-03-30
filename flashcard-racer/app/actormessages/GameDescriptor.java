package actormessages;

import java.util.List;

import akka.actor.ActorRef;
import dtos.Card;

public class GameDescriptor {
	private final ActorRef backingActor;
	private final String path;
	private String pin;
	private List<Card> cards;
	private List<String> players;
	
	public GameDescriptor(ActorRef backingActor, String pin, String path) {
		super();
		this.path = path;
		this.pin = pin;
		this.backingActor = backingActor;
	}

	public String getPath() {
		return path;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public ActorRef getBackingActor() {
		return backingActor;
	}

	@Override
	public String toString() {
		return "GameDescriptor [backingActor=" + backingActor + ", path=" + path + ", pin=" + pin + ", cards=" + cards + ", players=" + players + "]";
	}

	public List<String> getPlayers() {
		return players;
	}

	public void setPlayers(List<String> players) {
		this.players = players;
	}
	
	
}
