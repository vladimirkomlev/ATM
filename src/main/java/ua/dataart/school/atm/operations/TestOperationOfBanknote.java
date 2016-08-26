package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class TestOperationOfBanknote {

	static InputStream inputStream = TestOperationOfBanknote.class.getClassLoader().getResourceAsStream("banknote.properties");
	
	
	public static void main(String [] args) throws IOException, CloneNotSupportedException {
		OperationOfBanknote operationOnBanknote=new OperationOfBanknote(inputStream);
		BanknoteStorage storage=new BanknoteStorage();
		storage.getBanknotes().get(0).setValue(500);
		storage.getBanknotes().get(0).setCount(10);
//		storage.getBanknotes().get(1).setValue(200);
//		storage.getBanknotes().get(1).setCount(4);
		int saveAmount= operationOnBanknote.getCash(storage);
		operationOnBanknote.saveCurrentStorageInMemory();
		int saveAmout2=operationOnBanknote.getCash(storage);
	}
}
