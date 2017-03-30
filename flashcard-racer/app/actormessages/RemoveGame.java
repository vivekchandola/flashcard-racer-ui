package actormessages;

import akka.actor.ActorRef;

public class RemoveGame {
	private final ActorRef game;

	public RemoveGame(ActorRef game) {
		super();
		this.game = game;
	}

	public ActorRef getGame() {
		return game;
	}
}
