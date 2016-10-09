package ua.dataart.school.atm.operations;

import java.io.IOException;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.behavior.RequiredAmount;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class InputAmountForAcceptInputCash extends RequiredAmount {
	
	private static final Logger LOG=Logger.getLogger(InputAmountForAcceptInputCash.class);

	private BanknoteStorage inputStorageOfBnknote;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int inputAmount;
	private int resultAmount;
	private String outputMessage;
	
	public InputAmountForAcceptInputCash(BanknoteStorage inputStorageOfBanknote, OperationOfBanknoteImpl operationOfBanknote) {
		this.inputStorageOfBnknote=inputStorageOfBanknote;
		this.operationOfBanknote=operationOfBanknote;
	}
	
	@Override
	public boolean searchOfTheRequiredAmount() throws IOException {
		initRequiredAndResultAmount();
		LOG.info("inputAmount="+inputAmount);
		LOG.info("resultAmount="+resultAmount);
		if(resultAmount==0){
			outputMessage=MESSAGE_BANKNOTE_IS_FULL;
			return true;
		}else if(inputAmount>resultAmount){
			return getResultInputOverResultAmount();
		}else{
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
		if(inputAmount==resultAmount){
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			outputMessage = MESSAGE_CDERITED_AMOUNT + resultAmount;
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void initRequiredAndResultAmount() throws IOException {
		inputAmount=operationOfBanknote.getAmountFromInputStorageOfBanknotes(inputStorageOfBnknote.getBanknotes());
		try {
			resultAmount=operationOfBanknote.acceptInputCash(inputStorageOfBnknote);
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

}
