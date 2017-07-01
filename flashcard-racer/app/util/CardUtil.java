package util;

import java.util.Random;

import dtos.Card;
import models.enums.Difficulty;

public final class CardUtil {

    /**
     * generate new cards based on enum difficulty
     */
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


    /**
     * 
     * generate new cards based on enum difficulty
     * @param userChoice
     * @return
     */
    public static Card randomPracticeCard(Difficulty userChoice) {

        Random random = new Random();
        String operator = userChoice.getListOps().get(random.nextInt(userChoice.getListOps().size()));
        int firstNumber = random.nextInt(userChoice.getMaxNumber() - userChoice.getMinNumber() + 1) + userChoice.getMinNumber();
        int secondNumber = random.nextInt(userChoice.getMaxNumber() - userChoice.getMinNumber() + 1) + userChoice.getMinNumber();
        if ("/".equals(operator)) {
            firstNumber = firstNumber * secondNumber;
        }
        return new Card(firstNumber, secondNumber, operator);
    }

    /**
     * 
     * evaluate the submitted answer
     * @param card
     * @return
     */
    public static boolean evaluate(Card card) {
        if (card == null || card.getResult() == null ) {
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

}
