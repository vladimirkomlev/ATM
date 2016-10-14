package ua.dataart.school.atm.domain;

public class Banknote implements Cloneable{

	private int value;
	private int count;

	public Banknote(int value, int count) {
		this.value = value;
		this.count = count;
	}

	public int getValue() {
		return value;
	}

	public int getCount() {
		return count;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Banknote:[value=" + value + ", count=" + count + "]";
	}

	@Override
	public Banknote clone() throws CloneNotSupportedException {
			return (Banknote)super.clone();

	}

}
