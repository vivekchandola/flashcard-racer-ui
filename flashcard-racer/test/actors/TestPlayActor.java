package actors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.avaje.ebeaninternal.server.type.ScalarTypeJsonMapPostgres.JSON;
import com.fasterxml.jackson.databind.JsonNode;
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
import play.libs.Json;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CardUtil.class)
public class TestPlayActor {
    
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
    public void testReceiveObject(){
        new JavaTestKit(system) {{

            final JavaTestKit gameProbe = new JavaTestKit(system);
            final JavaTestKit outProbe = new JavaTestKit(system);
            
            final Props props = Props.create(PlayerActor.class, outProbe.getRef(), gameProbe.getRef(),"Student1");
            final ActorRef subject = system.actorOf(props);
             
            Card msg = new Card();
                       
            subject.tell(msg, getRef());
            String actual = outProbe.expectMsgClass(String.class);
            JsonNode jsonNode = Json.parse(actual);
            Card c = Json.fromJson(jsonNode, Card.class);
            
            assertEquals(c.getA(), msg.getA());
            
            // compare c and msg
        }
        };
    }


}
