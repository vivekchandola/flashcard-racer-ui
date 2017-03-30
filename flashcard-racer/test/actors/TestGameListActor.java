package actors;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import actormessages.AddGame;
import actormessages.RemoveGame;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;

public class TestGameListActor {

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
	public void testRemoveGame(){
		new JavaTestKit(system) {
			{
				final Props props = Props.create(GameListActor.class);				
				
				final ActorRef subject = system.actorOf(props);
				final JavaTestKit probe = new JavaTestKit(system);
				
				RemoveGame g = mock(RemoveGame.class);
				
				subject.tell(g, getRef());
				
				expectMsgEquals(duration("1 second"), "removed");
				verify(g).getGame();
			}
		};		
	}
	
	@Test
	public void testAddGame(){
		new JavaTestKit(system) {
			{
				final Props props = Props.create(GameListActor.class);
				
				
				final ActorRef subject = system.actorOf(props);

				AddGame msg  = mock(AddGame.class);
				
				subject.tell(msg, getRef());
				
				expectMsgEquals(duration("1 second"), "done");
								
				verify(msg).getPin();
				verify(msg).getGame();
				
			}
		};		
	}

}
