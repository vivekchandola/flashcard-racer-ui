package actors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actormessages.AllDone;
import actormessages.CardSolved;
import actormessages.NextCard;
import actormessages.ObserveRequest;
import actormessages.ProgressIndicator;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import dtos.Card;
import play.libs.Json;

public class PlayerActor extends UntypedActor {
	private static final Logger logger = LoggerFactory.getLogger(PlayerActor.class);

	private String userId;
	private ActorRef out;
	private ActorRef game;
	private Card currentCard;

	private long currCardStart;
	private long currCardEnd;

	public PlayerActor(ActorRef out, ActorRef game, String userId) {
		this.out = out;
		this.game = game;
		this.userId = userId;
		this.currentCard = null;
	}

	@Override
	public void preStart() {
		game.tell(new ObserveRequest(), getSelf());
		game.tell(new NextCard(userId), self());
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof String) {
			Card c = Json.fromJson(Json.parse((String) msg), Card.class);
			CardSolved solved = new CardSolved(userId, c);
			game.tell(solved, self());
		} else if (msg instanceof Card) {
			currentCard = (Card) msg;
			currCardStart = System.currentTimeMillis();
			out.tell(Json.toJson(currentCard).toString(), self());
		} else if (msg instanceof Boolean) {
			if ((Boolean) msg) {
				currCardEnd = System.currentTimeMillis();
				logger.info("User {} answered {} in {} seconds", userId, currentCard, (currCardStart - currCardEnd) / 10000L);
				game.tell(new NextCard(userId), self());
			} else {
				out.tell(Json.toJson(currentCard).toString(), self());
			}
		} else if (msg instanceof AllDone) {
			self().tell(PoisonPill.getInstance(), self());
		} else if (msg instanceof ProgressIndicator) {
			out.tell(Json.toJson(msg).toString(), self());
		}
	}

}
