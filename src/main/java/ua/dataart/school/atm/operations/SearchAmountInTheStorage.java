package ua.dataart.school.atm.operations;

import java.io.IOException;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.storage.BanknoteStorage;

public class SearchAmountInTheStorage {
	
	private final static Logger LOG = Logger.getLogger(SearchAmountInTheStorage.class);
	private final static String MESSAGE_INPUT_ZERO = "Entered amount should be greater than zero. Please, enter the correct value.";
	private final static String MESSAGE_AMOUNT_IS_MISSING = "Unfortunately the required banknotes is missing in the ATM.";
	private final static String MESSAGE_ANOTHER_SUM_PATH_FIRST = "Unfortunately we can not give you the required amount in the requested banknotes. We can give you sum ";
	private final static String MESSAGE_ANOTHER_SUM_PATH_SECOND = " the following denominations of banknotes:";
	private final static String MESSAGE_YOU_GOT = "You got: ";
	
	private BanknoteStorage inputValuesStorageOfBanknote;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int requiredSum;
	private int resultSum;
	private String outputMassage;
	
	public SearchAmountInTheStorage(BanknoteStorage inputStorageOfBanknote, OperationOfBanknoteImpl operationOfBanknote) {
		this.inputValuesStorageOfBanknote = inputStorageOfBanknote;
		this.operationOfBanknote = operationOfBanknote;
	}
	
	public boolean searchOfTheRequiredAmount() throws IOException {
		requiredSum = operationOfBanknote.getAmountFromInputStorageOfBanknotes(inputValuesStorageOfBanknote.getBanknotes());
		try {
			resultSum = operationOfBanknote.giveRequiredCash(inputValuesStorageOfBanknote);
		} catch (CloneNotSupportedException e) {
			LOG.info("The cloning operation failed. " + e.fillInStackTrace());
		}

		if (requiredSum == 0) {
			outputMassage = MESSAGE_INPUT_ZERO;
			return true;
		} else if (resultSum == 0 || requiredSum > resultSum) {
			return recivingMissingAmount();
		} else {
			return equalsRequiredAndResultSum();
		}
	}
	
	private boolean recivingMissingAmount(){
		resultSum = operationOfBanknote.giveRemainingAmountOfCash();
		if(resultSum == 0){
			outputMassage = MESSAGE_AMOUNT_IS_MISSING;
			return true;
		} else {
			outputMassage = MESSAGE_ANOTHER_SUM_PATH_FIRST + resultSum + MESSAGE_ANOTHER_SUM_PATH_SECOND;
			inputValuesStorageOfBanknote.setBanknotes(operationOfBanknote.getSaveStorage());
			return false;
		}	
	}
	
	private boolean equalsRequiredAndResultSum() {
		if (requiredSum == resultSum) {
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			outputMassage = MESSAGE_YOU_GOT + resultSum;
			return true;
		} else {
			return false;
		}
	}
	
	public int getResultSum() {
		return resultSum;
	}
	
	public String getOutputMassage() {
		return outputMassage;
	}
}
