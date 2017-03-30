package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.base.Strings;

import dtos.Card;
import dtos.UserSelectedChoice;
import models.enums.Difficulty;
import play.data.DynamicForm;

public final class CardUtil {

	public static Card randomCard(Difficulty diff) {
		Random rand = new Random();
		String operator;
		int firstNumber;
		int secondNumber;

		switch (diff) {
		case MEDIUM:
			operator = rand.nextBoolean() ? "*" : (rand.nextBoolean() ? "-" : "+");
			if ("*".equals(operator)) {
				firstNumber = generateInteger(rand, 10);
				secondNumber = generateInteger(rand, 10);
			} else {
				firstNumber = generateInteger(rand, 20);
				secondNumber = generateInteger(rand, 20);
			}
			break;
		case HARD:
			operator = rand.nextBoolean() ? "*" : (rand.nextBoolean() ? "/" : (rand.nextBoolean() ? "+" : "-"));

			if ("/".equals(operator)) {
				secondNumber = generateInteger(rand, 20);
				firstNumber = generateInteger(rand, 20) * secondNumber;
			} else if ("*".equals(operator)) {
				firstNumber = generateInteger(rand, 20);
				secondNumber = generateInteger(rand, 10);
			} else {
				firstNumber = generateInteger(rand, 20);
				secondNumber = generateInteger(rand, 20);
			}

			break;
		case EASY:
		default:
			firstNumber = generateInteger(rand, 20);
			secondNumber = generateInteger(rand, 20);
			operator = rand.nextBoolean() ? "+" : "-";

			if ("-".equals(operator) && secondNumber > firstNumber) {
				int tmp = secondNumber;
				secondNumber = firstNumber;
				firstNumber = tmp;
			}

			break;
		}

		return new Card(firstNumber, secondNumber, operator);

	}

	private static int generateInteger(Random rand, int multiplier) {
		return 1 + rand.nextInt(multiplier - 1);
	}

	public static Card randomCard(UserSelectedChoice userChoice) {

		Random random = new Random();
		String operator = userChoice.getOperators().get(random.nextInt(userChoice.getOperators().size()));
		int firstNumber = random.nextInt(userChoice.getRange()) + userChoice.getMinimumNumber();
		int secondNumber = random.nextInt(userChoice.getRange()) + userChoice.getMinimumNumber();
		if ("/".equals(operator)) {
			firstNumber = firstNumber * secondNumber;
		}
		return new Card(firstNumber, secondNumber, operator);
	}

	public static boolean evaluate(Card card) {
		if (card == null) {
			return false;
		}

		switch (card.getOp()) {
		case "+":
			return card.getA() + card.getB() == card.getResult();
		case "-":
			return card.getA() - card.getB() == card.getResult();
		case "*":
			return card.getA() * card.getB() == card.getResult();
		case "/":
			return card.getA() / card.getB() == card.getResult();
		default:
			return false;
		}
	}

	public static List<Card> loadCards(DynamicForm form) {
		List<Card> cardLists = new ArrayList<>();
		int size = Integer.parseInt(form.data().get("size"));
		for (int i = 0; i < size; i++) {
			String firstNumber = form.data().get("firstnumber" + i);
			String operator = form.data().get("operator" + i);
			String secondnumber = form.data().get("secondnumber" + i);
			String discard = form.data().get("discard" + i);
			if(Strings.isNullOrEmpty(discard))
			cardLists.add(new Card(Integer.parseInt(firstNumber), Integer.parseInt(secondnumber), operator));
		}
		return cardLists;
	}
}
