package ua.dataart.school.atm.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BanknoteTest {
	
	private Banknote banknote;
	
	@Before
	public void init() {
		banknote=new Banknote(10, 10);
		banknote.setCount(10);
		banknote.setValue(10);
		banknote=new Banknote(10, 10);
	}
	
	@Test
	public void testBabknote() {
		assertThat(banknote.getCount(), is(10));
		assertThat(banknote.getValue(), is(10));
		assertThat(banknote.toString(), is("Banknote:[value="+10+", count="+10+"]"));
	}
}
