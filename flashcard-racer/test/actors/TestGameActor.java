package actors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

import actormessages.GameDescriptor;
import actormessages.GetGameDescription;
import actormessages.GameDescriptor;
import actormessages.AllDone;

import actormessages.JoinGame;
import actormessages.NextCard;
import actormessages.RemoveGame;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import akka.testkit.JavaTestKit;

import dtos.Card;
import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
import util.CardUtil;
import util.HasherSalter;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CardUtil.class)
public class TestGameActor {

	static ActorSystem system;

	@BeforeClass
	public static void setup() {
		system = ActorSystem.create();
	}

	@AfterClass
	public static void teardown() {
		JavaTestKit.shutdownActorSystem(system);
		system = null;
	}

	@Test
	public void testJoin() {
		assertIsJoined("pin", "pin", true);
	}

	@Test
	public void testDoNotJoin() {
		assertIsJoined("pin", "pin2", false);
	}

	private void assertIsJoined(String requiredPin, String usedPin, boolean expectTojoin) {
		new JavaTestKit(system) {
			{
				final ActorRef subject = getGameWithPin(requiredPin);

				joinGameWithPin(usedPin, subject);

				expectMsgEquals(expectTojoin);

			}

			private void joinGameWithPin(String usedPin, final ActorRef subject) {
				JoinGame msg = new JoinGame(usedPin, null);
				subject.tell(msg, getRef());
			}

		};
	}

	private ActorRef getGameWithPin(String requiredPin) {
		List<Card> mockList = mock(List.class);

		final Props props = Props.create(GameActor.class, requiredPin, mockList);

		final ActorRef subject = system.actorOf(props);
		return subject;
	}

	@Test
	public void testGetGameDescription() {
		new JavaTestKit(system) {
			{
				final ActorRef subject = getGameWithPin("");

				GetGameDescription msg = new GetGameDescription();

				subject.tell(msg, getRef());

				GameDescriptor desc = expectMsgClass(GameDescriptor.class);
			}

			private void joinGameWithPin(String usedPin, final ActorRef subject) {
				JoinGame msg = new JoinGame(usedPin, null);
				subject.tell(msg, getRef());
			}

		};
	}

}
