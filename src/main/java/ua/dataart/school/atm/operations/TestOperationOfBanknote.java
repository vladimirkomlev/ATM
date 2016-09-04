package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.io.InputStream;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class TestOperationOfBanknote {

	static InputStream inputStream = TestOperationOfBanknote.class.getClassLoader().getResourceAsStream("banknote.properties");
	
	
	public static void main(String [] args) throws IOException, CloneNotSupportedException {
		OperationOfBanknote operationOnBanknote=new OperationOfBanknote(inputStream);
		BanknoteStorage storage=new BanknoteStorage();
		BanknoteStorage storageForPutCash=new BanknoteStorage();
		storage.getBanknotes().get(0).setValue(500);
		storage.getBanknotes().get(0).setCount(2);
		storage.getBanknotes().get(2).setValue(100);
		storage.getBanknotes().get(2).setCount(1);
		storageForPutCash.getBanknotes().get(0).setValue(500);
		storageForPutCash.getBanknotes().get(0).setCount(1);
		storageForPutCash.getBanknotes().get(2).setValue(100);
		storageForPutCash.getBanknotes().get(2).setCount(1);
		int saveAmount= operationOnBanknote.giveRequiredCash(storage);
		operationOnBanknote.giveRemainingAmountOfCash();
		operationOnBanknote.saveCurrentStorageInMemory();
		int saveAmount3=operationOnBanknote.acceptInputCash(storageForPutCash);
		int saveAmout2=operationOnBanknote.giveRequiredCash(storage);
	}
}
