package dtos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

import models.enums.Difficulty;
import util.CardUtil;


public class PracticeTest {
	
	@Test
	public void testCardFactory() {
		Card cardEasy = CardUtil.randomCard(Difficulty.EASY);
		assertSignIsEither(cardEasy, "+","-");

		Card cardMedium = CardUtil.randomCard(Difficulty.MEDIUM);
		assertSignIsEither(cardMedium, "+","-","*");
		
		Card cardHard = CardUtil.randomCard(Difficulty.HARD);
		assertSignIsEither(cardHard, "+","-","*","/");
	}


	private void assertSignIsEither(Card cardHard, String... list) {
		boolean correctSign = Arrays.stream(list)
				.anyMatch(s -> s == cardHard.getOp());
		
		assertTrue(correctSign);
	}
	
	@Test
	public void testEvaluateCorrectAnswer() throws ScriptException {
		testEvaluateCorrectAnswer(Difficulty.EASY);
		testEvaluateCorrectAnswer(Difficulty.MEDIUM);
		testEvaluateCorrectAnswer(Difficulty.HARD);		
	}
	
	private void testEvaluateCorrectAnswer(Difficulty diff) throws ScriptException{
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("javascript");

		{
			Card card = CardUtil.randomCard(diff);
			String op = card.getOp();
			String exp = card.getA() + op + card.getB();
			Integer result = getIntegerResult(engine, exp);
			
			card.setResult(result);

			assertTrue(CardUtil.evaluate(card));
		}		
	}
	
	private Integer getIntegerResult(ScriptEngine engine, String exp) throws ScriptException{
		Object result = engine.eval(exp);
		
		if (result instanceof Double) {
			return ((Double) result).intValue();
		}
		
		return (Integer) result;
	}	
	
	@Test
	public void testEvaluateWrongAnswer() throws ScriptException {
			assertWrongAnswerWithDiff(Difficulty.EASY);
			assertWrongAnswerWithDiff(Difficulty.MEDIUM);
			assertWrongAnswerWithDiff(Difficulty.HARD);
		
	}

	private void assertWrongAnswerWithDiff(Difficulty diff) throws ScriptException {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("javascript");

		Card card = CardUtil.randomCard(diff);
		String op = card.getOp();
		String exp = card.getA() + op + card.getB();
		Integer result = getIntegerResult(engine, exp);
		
		card.setResult(result + (1 + new Random().nextInt(10)));

		assertFalse(CardUtil.evaluate(card));		
	}
	
	@Test
	public void testCreateSession() {
		testCreatSessionWithDifficulty(Difficulty.EASY);
		testCreatSessionWithDifficulty(Difficulty.MEDIUM);
		testCreatSessionWithDifficulty(Difficulty.HARD);
	}

	private void testCreatSessionWithDifficulty (Difficulty diff) {
		{
			PracticeSession session = PracticeSession.startSession(diff);
			assertEquals(0, session.getCurrent());
			assertEquals(0, session.getNumCorrect());
			assertSessionLength(session);
			assertEquals(diff, session.getDifficulty());
		}
	}
	
	private void assertSessionLength(PracticeSession session){
		if(session.getDifficulty() == Difficulty.EASY)
			assertEquals(5, session.getSessionLength());
		
		if(session.getDifficulty() == Difficulty.MEDIUM)
			assertEquals(10, session.getSessionLength());
		
		if(session.getDifficulty() == Difficulty.HARD)
			assertEquals(20, session.getSessionLength());
		
	}
	
	@Test
	public void testAdvance() {
		PracticeSession session = PracticeSession.startSession(Difficulty.EASY);

		assertEquals(0, session.getCurrent());
		assertEquals(0, session.getNumCorrect());

		session.getCard().setA(10);
		session.getCard().setB(20);
		session.getCard().setOp("+");
		session.getCard().setResult(30);

		Card oldCard = session.getCard();

		PracticeSession.advanceSession(session);

		assertEquals(1, session.getCurrent());
		assertEquals(1, session.getNumCorrect());
		assertTrue(oldCard != session.getCard());
	}
}
