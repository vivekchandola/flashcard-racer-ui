package actormessages;

import akka.actor.ActorRef;

public class AddGame {

	private final ActorRef game;
	private final String pin;
	
	public AddGame(String pin, ActorRef game) {
		super();
		this.game = game;
		this.pin = pin;
	}

	public ActorRef getGame() {
		return game;
	}

	public String getPin() {
		return pin;
	}

}
