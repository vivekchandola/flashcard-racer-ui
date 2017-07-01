package dtos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Card pojo class
 * Hold numbers and opeartor
 * @author Vivek
 *
 */
public class Card {
	private static final Pattern p = Pattern.compile("(\\d*)\\s*(\\+|-|\\*|/)\\s*(\\d*)");
	private int a;
	private int b;
	private String op;
	private Integer result;

	public Card(String c) {
		Matcher m = p.matcher(c);
		if (m.matches()) {
			a = Integer.parseInt(m.group(1).trim());
			b = Integer.parseInt(m.group(3).trim());
			op = m.group(2).trim();
		} else {
			throw new IllegalArgumentException("Argument not mathematical expression!");
		}
	}

	public Card() {

	}

	public Card(int a, int b, String op) {
		super();
		this.a = a;
		this.b = b;
		this.op = op;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public void setA(int a) {
		this.a = a;
	}

	public void setB(int b) {
		this.b = b;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "Card [a=" + a + ", b=" + b + ", op=" + op + ", result=" + result + "]";
	}

}
