package dtos;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;

public class UserSelectedChoice {

	private final int numberOfQuestions;

	private final int timer;

	private final int maximumNumber;

	private final int minimumNumber;
	
	private final int range;

	private final List<String> operators;

	public UserSelectedChoice(String numberOfQuestions, String timer, String maximumNumber, String minimumNumber,
			String add, String sub, String multi, String div) {

		this.maximumNumber = formatInput(maximumNumber, 10);
		this.numberOfQuestions = formatInput(numberOfQuestions, 10);
		this.timer = formatInput(timer, 20);
		this.minimumNumber = formatInput(minimumNumber, 0);
		this.operators = getOps(add, sub, multi, div);
		this.range = getMaximumNumber() - getMinimumNumber() + 1;

	}

	private List<String> getOps(String add, String sub, String multi, String div) {
		List<String> ops = new ArrayList<>();
		addOperator(add, "+", ops);
		addOperator(sub, "-", ops);
		addOperator(multi, "*", ops);
		addOperator(div, "/", ops);
		return ops;
	}

	private int formatInput(String value, int defaultValue) {
		if (!Strings.isNullOrEmpty(value)) {
			try {
				return Integer.parseInt(value);
			} catch (Exception e) {
			}
		}
		return defaultValue;
	}

	private void addOperator(String operator, String value, List<String> ops) {
		if (!Strings.isNullOrEmpty(operator)) {
			ops.add(value);
		}
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public int getTimer() {
		return timer;
	}

	public int getMaximumNumber() {
		return maximumNumber;
	}

	public int getMinimumNumber() {
		return minimumNumber;
	}

	public List<String> getOperators() {
		return operators;
	}

	public int getRange() {
		return range;
	}

}
