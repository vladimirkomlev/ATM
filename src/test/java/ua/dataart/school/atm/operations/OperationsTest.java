package ua.dataart.school.atm.operations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class OperationsTest {
	
	private InputStream inputStream=OperationsTest.class.getClassLoader().getResourceAsStream("banknote.properties");
	private OperationOfBanknote operationOfBanknote;
	private BanknoteStorage banknoteStorage;
	
	@Before
	public void init(){
		operationOfBanknote=new OperationOfBanknote(inputStream);
		banknoteStorage=new BanknoteStorage();
		banknoteStorage.getBanknotes().get(0).setValue(500);
		banknoteStorage.getBanknotes().get(0).setCount(2);
	}
	
	@Test
	public void testOperations() throws IOException, CloneNotSupportedException{
		assertThat(operationOfBanknote.getCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknote.putCash(banknoteStorage), is(1000));
		assertThat(operationOfBanknote.getCashWhenResultSumLessRequiredSum(), is(1000));
		assertThat(operationOfBanknote.getSumFromBanknoteStorageInput(banknoteStorage.getBanknotes()), is(1000));
		assertThat(operationOfBanknote.getValueOfNameFromFile("maxCount"), is(20));
	}
}
