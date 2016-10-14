package ua.dataart.school.atm.operations.behavior;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;

public abstract class DifferentAmount {

	private static final Logger LOG = Logger.getLogger(DifferentAmount.class);
	protected static final String CONFIRM = "Confirm";
	protected static final String CANCEL = "Cancel";
	protected static final String MESSAGE_EMPTY = "";

	public abstract boolean selectAnotherAmount(HttpServletRequest request);

	public abstract String getOutputMessage();

	public abstract boolean getResultChoose();

	protected void saveInfoTransaction(List<Banknote> listOfBanknote, String infoMessage) {
		List<Banknote> savedListOfBanknote = listOfBanknote;
		StringBuilder sbResult = new StringBuilder();
		sbResult.append(infoMessage);
		for (Banknote banknote : savedListOfBanknote) {
			if (banknote.getCount() != 0) {
				sbResult.append(banknote.toString());
				sbResult.append(";");
			}
		}
		sbResult.deleteCharAt(sbResult.length() - 1);
		LOG.info(sbResult);
	}

}
