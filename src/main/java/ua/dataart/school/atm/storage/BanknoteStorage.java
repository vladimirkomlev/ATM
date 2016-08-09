package ua.dataart.school.atm.storage;

import java.util.ArrayList;
import java.util.List;

import ua.dataart.school.atm.domain.Banknote;

public class BanknoteStorage {

	private List<Banknote> banknotes = new ArrayList<>();

	public BanknoteStorage() {
		Banknote banknote1 = new Banknote(0, 0);
		Banknote banknote2 = new Banknote(0, 0);
		Banknote banknote3 = new Banknote(0, 0);
		Banknote banknote4 = new Banknote(0, 0);
		Banknote banknote5 = new Banknote(0, 0);

		banknotes.add(banknote1);
		banknotes.add(banknote2);
		banknotes.add(banknote3);
		banknotes.add(banknote4);
		banknotes.add(banknote5);
	}

	public List<Banknote> getBanknotes() {
		return banknotes;
	}

	public void setBanknotes(List<Banknote> banknotes) {
		this.banknotes = banknotes;
	}

	public String toString() {
		return "BanknoteStorage:[" + banknotes + "]";
	}

}
