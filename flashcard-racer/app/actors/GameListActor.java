package actors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import actormessages.AddGame;
import actormessages.GameDescriptor;
import actormessages.GetGame;
import actormessages.ListGames;
import actormessages.RemoveGame;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class GameListActor extends UntypedActor {

	private Map<String, ActorRef> games = new HashMap<String, ActorRef>();

	@Override
	public void onReceive(Object msg) throws Throwable {
		if (msg instanceof AddGame) {
			
			AddGame addGameMsg = (AddGame) msg;
			games.put(addGameMsg.getPin(), addGameMsg.getGame());
			
			getSender().tell("done", getSelf());
	
		} else if (msg instanceof RemoveGame) {
		
			games.remove(((RemoveGame) msg).getGame());
			getSender().tell("removed", getSelf());
		
		} else if (msg instanceof ListGames) {
			
			List<GameDescriptor> listOfGames = games.entrySet().stream().map(e -> new GameDescriptor(e.getValue(), e.getKey(), e.getValue().path().toSerializationFormat()))
					.collect(Collectors.toList());
			getSender().tell(listOfGames, getSelf());
			
		} else if (msg instanceof GetGame) {
			
			String pin = ((GetGame) msg).getPin();
			ActorRef game = games.get(pin);
			game.tell(msg, getSelf());
			getSender().tell(new GameDescriptor(game, pin, game.path().toSerializationFormat()), getSelf());
			
		}
	}
}