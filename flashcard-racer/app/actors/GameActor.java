package actors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import actormessages.AllDone;
import actormessages.CardSolved;
import actormessages.GameDescriptor;
import actormessages.GetGameDescription;
import actormessages.JoinGame;
import actormessages.NextCard;
import actormessages.ObserveRequest;
import actormessages.ProgressIndicator;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import dtos.Card;
import util.CardUtil;

public class GameActor extends UntypedActor {
	private String pin;
	private Map<String, Integer> players = new HashMap<String, Integer>();
	private List<ActorRef> observers = new ArrayList<ActorRef>();

	private List<Card> cards = new ArrayList<Card>();

	public GameActor(String pin, List<Card> cards) {
		this.pin = pin;
		this.cards = cards;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof JoinGame) {
			if (pin.equals(((JoinGame) msg).getPin())) {
				players.put(((JoinGame) msg).getUserId(), 0);
				getSender().tell(Boolean.TRUE, self());
			} else {
				getSender().tell(Boolean.FALSE, self());
			}
		} else if (msg instanceof NextCard) {
			int nextCardNumber = players.get(((NextCard) msg).getUserId());
			if (nextCardNumber >= cards.size()) {
				// Do nothing
			} else {
				Card c = cards.get(nextCardNumber);
				getSender().tell(c, getSelf());
			}
		} else if (msg instanceof GetGameDescription) {
			GameDescriptor gd = new GameDescriptor(getSelf(), pin, getSelf().path().toSerializationFormat());
			gd.setCards(this.cards);
			gd.setPlayers(new ArrayList<String>(players.keySet()));
			getSender().tell(gd, getSelf());
		} else if (msg instanceof CardSolved) {
			boolean solved = CardUtil.evaluate(((CardSolved) msg).getCard());

			if (solved) {
				getSender().tell(Boolean.TRUE, self());
				int val = players.computeIfPresent(((CardSolved) msg).getUserId(), (k, v) -> v + 1);

				for (ActorRef ob : observers) {
					float progress = (float) val / (float) cards.size() * 100;
					ob.tell(new ProgressIndicator(((CardSolved) msg).getUserId(), (int) progress), getSelf());
				}
				
				if (everyoneDone()) {
					observers.forEach(ob -> {
						ob.tell(new AllDone(), getSelf());
					});
				}
			} else {
				getSender().tell(Boolean.FALSE, self());
			}
		} else if (msg instanceof ObserveRequest) {
			for (Map.Entry<String, Integer> e : players.entrySet()) {
				float progress = (float) e.getValue() / (float) cards.size() * 100;
				getSender().tell(new ProgressIndicator(e.getKey(), (int) progress), getSelf());
			}
			observers.add(getSender());
		}
	}

	private boolean everyoneDone() {
		return Long.compare(0L, players.values().stream().filter(i -> i < cards.size()).count()) == 0;
	}
}
