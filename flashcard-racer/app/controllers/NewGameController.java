package controllers;

import java.util.List;
import java.util.function.Function;

import javax.inject.Named;

import com.google.inject.Inject;

import actormessages.AddGame;
import actormessages.GameDescriptor;
import actormessages.GetGame;
import actormessages.GetGameDescription;
import actormessages.JoinGame;
import actormessages.ListGames;
import actors.GameActor;
import actors.GameObserverActor;
import actors.PlayerActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import dtos.Card;
import dtos.NewGameSession;
import dtos.UserSelectedChoice;
import models.enums.Difficulty;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.LegacyWebSocket;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.WebSocket;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import util.CardUtil;

public class NewGameController extends Controller {

    @Inject
    private ActorSystem actorSystem;
    @Inject
    private FormFactory formFactory;

    @Inject
    @Named("gamelist-actor")
    private ActorRef gamelistActor;

    public Result initializeNewGame() throws Exception {
        List<GameDescriptor> games = ask(gamelistActor, new ListGames());
        return ok(views.html.newgame.render(games));
    }

    public Result createGame() throws Exception {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String value = requestData.get("radio1");
        if ("1".equals(value)) {
            return createGameFromTemplate(requestData);
        } else {
            return createGameWithOptions(requestData);
        }

    }

    public Result registerGame() {
        DynamicForm form = formFactory.form().bindFromRequest();

        String pin = form.data().get("pin");

        List<Card> cards = CardUtil.loadCards(form);

        ActorRef game = actorSystem.actorOf(Props.create(GameActor.class, pin, cards));
        gamelistActor.tell(new AddGame(pin, game), ActorRef.noSender());

        return redirect(routes.NewGameController.initializeNewGame());
    }

    @SuppressWarnings("deprecation")
    public LegacyWebSocket<String> gameWS() {
        final String userId = session("student");
        final ActorRef game = actorSystem.actorFor(session("gameAddress"));
        LegacyWebSocket<String> ws = WebSocket.withActor(new Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef out) {
                return Props.create(PlayerActor.class, out, game, userId);
            }
        });

        return ws;
    }
    
    @SuppressWarnings("deprecation")
    public LegacyWebSocket<String> observeGameWS(String gameAddress) {
        final ActorRef game = actorSystem.actorFor(gameAddress);
        LegacyWebSocket<String> ws = WebSocket.withActor(new Function<ActorRef, Props>() {
            @Override
            public Props apply(ActorRef out) {
                return Props.create(GameObserverActor.class, out, game);
            }
        });

        return ws;
    }

    public Result playGame(String path) {
        return ok(views.html.playgame.render());
    }

    public Result viewGame(String path) throws Exception {
        ActorRef game = actorSystem.actorFor(path);
        GameDescriptor g = ask(game, new GetGameDescription());
        return ok(views.html.viewgame.render(g));
    }

	public Result joinGame() {
		DynamicForm form = formFactory.form().bindFromRequest();
		String pin = form.data().get("pin");

		try {
			GameDescriptor game = ask(gamelistActor, new GetGame(pin));
			String gameAddress = game.getPath();
			ActorRef gameActor = actorSystem.actorFor(gameAddress);

			if ((boolean) ask(gameActor, new JoinGame(pin, session("student")))) {
				session("gameAddress", gameAddress);
				return redirect(routes.NewGameController.playGame(game.getPath()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Results.badRequest("Invalid pin!");
	}

    private Result createGameWithOptions(DynamicForm requestData) throws Exception {
        String add = requestData.get("add");
        String substract = requestData.get("substract");
        String multiply = requestData.get("multiply");
        String division = requestData.get("division");
        String questions = requestData.get("questions");
        String timelimit = requestData.get("timelimit");
        String maxnumber = requestData.get("maxnumber");
        String minnumber = requestData.get("minnumber");
        UserSelectedChoice userSelectedChoice = new UserSelectedChoice(questions, timelimit, maxnumber, minnumber, add,
                substract, multiply, division);
        NewGameSession gameSession = new NewGameSession().createCardDecks(userSelectedChoice);

        return ok(views.html.carddecks.render(gameSession));
    }

    private Result createGameFromTemplate(DynamicForm requestData) {
        Difficulty difficulty = Difficulty.valueOf(requestData.get("difficulty"));
        NewGameSession gameSession = new NewGameSession().createCardDecks(difficulty);
        return ok(views.html.carddecks.render(gameSession));
    }

    private <T> T ask(ActorRef actor, Object msg) throws Exception {
        Timeout timeout = new Timeout(Duration.create(1, "seconds"));
        scala.concurrent.Future<Object> future = Patterns.ask(actor, msg, timeout);
        T resp = (T) Await.result(future, timeout.duration());
        return resp;
    }
}
