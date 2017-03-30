package models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Difficulty {

	EASY(5, 20, getList("+", "-")), MEDIUM(10, 15, getList("+", "-", "*")), HARD(20, 10, getList("+", "-", "*", "/"));
	private final int number;
	private final int timer;
	private final List<String> listOps;

	Difficulty(int number, int timer, List<String> listOps) {
		this.number = number;
		this.timer = timer;
		this.listOps = listOps;
	}

	private static List<String> getList(String... string) {
		List<String> listOps = new ArrayList<>();
		for (String ops : string) {
			listOps.add(ops);
		}
		return listOps;
	}

	public int getNumber() {
		return number;
	}

	public int getTimer() {
		return timer;
	}

	public List<String> getListOps() {
		return listOps;
	}
}
