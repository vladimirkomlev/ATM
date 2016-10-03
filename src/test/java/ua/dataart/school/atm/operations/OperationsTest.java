package ua.dataart.school.atm.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class OperationsTest {
	
	private OperationOfBanknoteImpl operationOfBanknoteImpl;
	private BanknoteStorage banknoteStorage;
	
	@Before
	public void init() throws IOException{
		operationOfBanknoteImpl=new OperationOfBanknoteImpl();
		banknoteStorage=new BanknoteStorage();
		banknoteStorage.getBanknotes().get(0).setValue(500);
		banknoteStorage.getBanknotes().get(0).setCount(2);
	}
	
	@Test
	public void testOperations() throws IOException, CloneNotSupportedException{
		assertThat(operationOfBanknoteImpl.giveRequiredCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknoteImpl.acceptInputCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknoteImpl.giveRemainingAmountOfCash(), is(1000));
		assertThat(operationOfBanknoteImpl.getAmountFromInputStorageOfBanknotes(banknoteStorage.getBanknotes()), is(1000));
	}
}
