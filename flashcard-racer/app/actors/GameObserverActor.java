package actors;

import actormessages.AllDone;
import actormessages.ObserveRequest;
import actormessages.ProgressIndicator;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import play.libs.Json;

public class GameObserverActor extends UntypedActor {
	private ActorRef out;
	private ActorRef game;

	public GameObserverActor(ActorRef out, ActorRef game) {
		this.out = out;
		this.game = game;
	}

	@Override
	public void preStart() {
		game.tell(new ObserveRequest(), getSelf());
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof ProgressIndicator) {
			out.tell(Json.toJson(msg).toString(), self());
		} else if (msg instanceof AllDone) {
			self().tell(PoisonPill.getInstance(), self());
		}
	}

}
