package ua.dataart.school.atm.operations;

import java.io.IOException;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.behavior.RequiredAmount;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class RequiredAmountForGiveRequiredCash extends RequiredAmount {
	
	private static final Logger LOG = Logger.getLogger(RequiredAmountForGiveRequiredCash.class);
	
	private BanknoteStorage inputStorageOfBanknote;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int requiredAmount;
	private int resultAmount;
	private String outputMessage;
	
	public RequiredAmountForGiveRequiredCash(BanknoteStorage inputStoregeOfBanknote, OperationOfBanknoteImpl operationOfBanknote) {
		this.inputStorageOfBanknote=inputStoregeOfBanknote;
		this.operationOfBanknote=operationOfBanknote;
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
	protected void initRequiredAndResultAmount() throws IOException{
		requiredAmount=operationOfBanknote.getAmountFromInputStorageOfBanknotes(inputStorageOfBanknote.getBanknotes());
		try {
			resultAmount=operationOfBanknote.giveRequiredCash(inputStorageOfBanknote);
		} catch (CloneNotSupportedException e) {
			LOG.info("The cloning operation failed. " + e.fillInStackTrace());
		}
	}

	private boolean recivingMissingAmount() {
		resultAmount = operationOfBanknote.giveRemainingAmountOfCash();
		LOG.info("after reciving resultAmount="+resultAmount);
		if (resultAmount == 0) {
			outputMessage = MESSAGE_AMOUNT_IS_MISSING;
			return true;
		} else {
			outputMessage = MESSAGE_ANOTHER_REQUSTED_SUM_PATH_FIRST + resultAmount + MESSAGE_ANOTHER_REQUSTED_SUM_PATH_SECOND;
			inputStorageOfBanknote.setBanknotes(operationOfBanknote.getSaveStorage());
			for(Banknote banknote: inputStorageOfBanknote.getBanknotes()){
				LOG.info(banknote.getValue()+"="+banknote.getCount());
			}
			return false;
		}
	}
	
	private boolean equalityRequiredAndResultAmount() {
		LOG.info("resultAmount==resultAmount-"+(requiredAmount==resultAmount));
		if (requiredAmount == resultAmount) {
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			outputMessage = MESSAGE_YOU_GOT + resultAmount;
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
}
