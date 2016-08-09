package ua.dataart.school.atm.storage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ua.dataart.school.atm.domain.Banknote;

public class BanknoteStorageTest {

	private BanknoteStorage banknoteStorage=mock(BanknoteStorage.class);
	private BanknoteStorage storage=mock(BanknoteStorage.class);
	private BanknoteStorage banknoteStorage2;
	private BanknoteStorage storage2;
	private Banknote mockBanknote=mock(Banknote.class);
	
	@Before
	public void init(){
		mockBanknote.setCount(1);
		List<Banknote> listBanknotes=new ArrayList<>(5);
		listBanknotes.add(0, mockBanknote);
		banknoteStorage2 = new BanknoteStorage();
		storage2 = new BanknoteStorage();
		storage2.setBanknotes(listBanknotes);
	}
	
	@Test
	public void	testBanknoteStorage(){
		assertThat(banknoteStorage.getBanknotes(), is(storage.getBanknotes()));
		assertThat(banknoteStorage2.getBanknotes().get(0).getCount(), is(0));
		assertThat(banknoteStorage2.getBanknotes().get(0).getValue(), is(0));
		assertThat(storage2.getBanknotes().get(0).getCount(), is(0));
		assertThat(storage2.toString(), is("BanknoteStorage:[[Mock for Banknote, hashCode: " + mockBanknote.hashCode() + "]]"));
	}
	

	
	
}
