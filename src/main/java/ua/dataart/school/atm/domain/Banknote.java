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

	// TODO: 8/11/16 eugene - it's really strange that banknote can change values
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
		// TODO: 8/11/16 eugene - strange formatting
		// vova - What do is strange with formatting?
			return (Banknote)super.clone();

	}

}
