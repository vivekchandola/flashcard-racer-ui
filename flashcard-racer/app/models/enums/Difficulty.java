package models.enums;

import java.util.ArrayList;
import java.util.List;

public enum Difficulty {

	EASY(5, 20, 10,0,getList("+", "-")), MEDIUM(10, 15,20,5, getList("+", "-", "*")), HARD(20, 10,40,10, getList("+", "-", "*", "/")), CUSTOM(10, 20,10,0, getList("+", "-", "*", "/"));
	private int number;
	private int timer;
	private int maxNumber;
	private int minNumber;
	private List<String> listOps;
	

	Difficulty(int number, int timer,int maxNumber,int minNumber, List<String> listOps) {
		this.number = number;
		this.timer = timer;
		this.listOps = listOps;
		this.minNumber = minNumber;
		this.maxNumber = maxNumber;
	}

	
    public int getMaxNumber() {
        return maxNumber;
    }

    
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    
    public int getMinNumber() {
        return minNumber;
    }

    
    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    
    public void setNumber(int number) {
        this.number = number;
    }

    
    public void setTimer(int timer) {
        this.timer = timer;
    }

    private static List<String> getList(String... string) {
		List<String> listOps = new ArrayList<>();
		for (String ops : string) {
			listOps.add(ops);
		}
		return listOps;
	}

	
    public void setListOps(List<String> listOps) {
        this.listOps = listOps;
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
