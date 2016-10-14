package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.behavior.RequiredAmount;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class InputAmountForAcceptInputCash extends RequiredAmount {

	private static final Logger LOG = Logger.getLogger(InputAmountForAcceptInputCash.class);
	private static final String INFO_TRANSACTION = "Receipt in cash-";

	private BanknoteStorage inputStorageOfBnknote;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int inputAmount;
	private int resultAmount;
	private String outputMessage;

	public InputAmountForAcceptInputCash(BanknoteStorage inputStorageOfBanknote,
			OperationOfBanknoteImpl operationOfBanknote) {
		this.inputStorageOfBnknote = inputStorageOfBanknote;
		this.operationOfBanknote = operationOfBanknote;
	}

	@Override
	public boolean searchOfTheRequiredAmount() throws IOException {
		initRequiredAndResultAmount();
		if (resultAmount == 0) {
			outputMessage = MESSAGE_BANKNOTE_IS_FULL;
			return true;
		} else if (inputAmount > resultAmount) {
			return getResultInputOverResultAmount();
		} else {
			return equalityInputAndResultAmount();
		}
	}

	private boolean getResultInputOverResultAmount() {
		outputMessage = MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_FIRST + inputAmount + MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_SECOND
				+ resultAmount + MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_THIRD;
		inputStorageOfBnknote.setBanknotes(operationOfBanknote.getSaveStorage());
		return false;
	}

	private boolean equalityInputAndResultAmount() {
		if (inputAmount == resultAmount) {
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
				saveInformationInLog();
				outputMessage = MESSAGE_CDERITED_AMOUNT + resultAmount;
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void initRequiredAndResultAmount() throws IOException {
		inputAmount = operationOfBanknote.getAmountFromInputStorageOfBanknotes(inputStorageOfBnknote.getBanknotes());
		try {
			resultAmount = operationOfBanknote.acceptInputCash(inputStorageOfBnknote);
		} catch (CloneNotSupportedException e) {
			LOG.info("The cloning operation failed. " + e.fillInStackTrace());
		}
	}

	@Override
	public String getOutputMessage() {
		return outputMessage;
	}

	@Override
	public int getResultAmount() {
		return resultAmount;
	}

	@Override
	protected void saveInformationInLog() {
		List<Banknote> savedStorageOfBanknote = operationOfBanknote.getSaveStorage();
		StringBuilder sbResult = new StringBuilder();
		sbResult.append(INFO_TRANSACTION);
		for (Banknote banknote : savedStorageOfBanknote) {
			if (banknote.getCount() != 0) {
				sbResult.append(banknote.toString());
				sbResult.append(";");
			}
		}
		sbResult.deleteCharAt(sbResult.length() - 1);
		LOG.info(sbResult);
	}

}
