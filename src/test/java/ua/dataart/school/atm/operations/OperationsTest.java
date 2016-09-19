package ua.dataart.school.atm.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class OperationsTest {
	
	private OperationOfBanknote operationOfBanknote;
	private BanknoteStorage banknoteStorage;
	
	@Before
	public void init() throws IOException{
		operationOfBanknote=new OperationOfBanknote();
		banknoteStorage=new BanknoteStorage();
		banknoteStorage.getBanknotes().get(0).setValue(500);
		banknoteStorage.getBanknotes().get(0).setCount(2);
	}
	
	@Test
	public void testOperations() throws IOException, CloneNotSupportedException{
		assertThat(operationOfBanknote.giveRequiredCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknote.acceptInputCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknote.giveRemainingAmountOfCash(), is(1000));
		assertThat(operationOfBanknote.getAmountFromInputStorageOfBanknotes(banknoteStorage.getBanknotes()), is(1000));
	}
}
