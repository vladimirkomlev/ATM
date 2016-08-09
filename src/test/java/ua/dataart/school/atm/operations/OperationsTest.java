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
	private Operations operations;
	private BanknoteStorage banknoteStorage;
	
	@Before
	public void init(){
		operations=new Operations(inputStream);
		banknoteStorage=new BanknoteStorage();
		banknoteStorage.getBanknotes().get(0).setValue(500);
		banknoteStorage.getBanknotes().get(0).setCount(2);
		
	}
	
	@Test
	public void testOperations() throws IOException, CloneNotSupportedException{
		assertThat(operations.getCash(banknoteStorage), is(1000));
		assertThat(operations.putCash(banknoteStorage), is(1000));
		assertThat(operations.getCashWhenResultSumLessRequiredSum(), is(1000));
		assertThat(operations.getSumFromBanknoteStorageInput(banknoteStorage.getBanknotes()), is(1000));
		assertThat(operations.getValueOfNameFromFile("maxCount"), is(20));
	}
}
