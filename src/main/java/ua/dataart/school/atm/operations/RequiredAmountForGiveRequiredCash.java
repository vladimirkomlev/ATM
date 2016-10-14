package ua.dataart.school.atm.operations;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.behavior.RequiredAmount;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class RequiredAmountForGiveRequiredCash extends RequiredAmount {

	private static final Logger LOG = Logger.getLogger(RequiredAmountForGiveRequiredCash.class);
	private static final String INFO_TRANSACTION = "Give out cash-";

	private BanknoteStorage inputStorageOfBanknote;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int requiredAmount;
	private int resultAmount;
	private String outputMessage;

	public RequiredAmountForGiveRequiredCash(BanknoteStorage inputStoregeOfBanknote,
			OperationOfBanknoteImpl operationOfBanknote) {
		this.inputStorageOfBanknote = inputStoregeOfBanknote;
		this.operationOfBanknote = operationOfBanknote;
	}

	@Override
	public boolean searchOfTheRequiredAmount() throws IOException {
		initRequiredAndResultAmount();
		if (resultAmount == 0 || requiredAmount > resultAmount) {
			return recivingMissingAmount();
		} else {
			return equalityRequiredAndResultAmount();
		}
	}

	@Override
	protected void initRequiredAndResultAmount() throws IOException {
		requiredAmount = operationOfBanknote.getAmountFromInputStorageOfBanknotes(inputStorageOfBanknote.getBanknotes());
		try {
			resultAmount = operationOfBanknote.giveRequiredCash(inputStorageOfBanknote);
		} catch (CloneNotSupportedException e) {
			LOG.info("The cloning operation failed. " + e.fillInStackTrace());
		}
	}

	private boolean recivingMissingAmount() {
		resultAmount = operationOfBanknote.giveRemainingAmountOfCash();
		if (resultAmount == 0) {
			outputMessage = MESSAGE_AMOUNT_IS_MISSING;
			return true;
		} else {
			outputMessage = MESSAGE_ANOTHER_REQUSTED_SUM_PATH_FIRST + resultAmount
					+ MESSAGE_ANOTHER_REQUSTED_SUM_PATH_SECOND;
			inputStorageOfBanknote.setBanknotes(operationOfBanknote.getSaveStorage());
			return false;
		}
	}

	private boolean equalityRequiredAndResultAmount() {
		if (requiredAmount == resultAmount) {
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
				saveInformationInLog();
				outputMessage = MESSAGE_YOU_GOT + resultAmount;
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			return true;
		} else {
			return false;
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
