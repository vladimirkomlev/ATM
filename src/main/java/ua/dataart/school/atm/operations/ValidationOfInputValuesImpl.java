package ua.dataart.school.atm.operations;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NumberUtils;

import ua.dataart.school.atm.operations.behavior.ValidationOfInputValues;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class ValidationOfInputValuesImpl extends ValidationOfInputValues{
	
	private static final String MESSAGE_VALIDATION_IS_EMPTY_STRING = "You do not specify the amount of banknotes. Please, enter the amount of banknotes.";
	private static final String MESSAGE_VALIDATION_NEGATIVE_NUMBER = "One of the fields is the value specified negative number. Please, enter the correct value.";
	private static final String MESSAGE_VALIDATION_FOR_INTEGER = "One of the fields is the value specified was not a number. Please, enter the correct value.";

	private BanknoteStorage storageOfBanknotes;
	private String outputMessage;

	public ValidationOfInputValuesImpl(BanknoteStorage banknoteStorage) {
		this.storageOfBanknotes = banknoteStorage;
	}

	@Override
	protected boolean validationIsEmptyString(HttpServletRequest request) {
		boolean result = true;
		int sizeStorage = storageOfBanknotes.getBanknotes().size();
		int counter = 0;
		for (int index = 0; index < sizeStorage; index++) {
			if (request.getParameter(String.valueOf(index)) == "") {
				counter++;
			}
		}
		if (counter == sizeStorage) {
			result=false;
			return result;
		} else {
			return result;
		}
	}

	@Override
	protected boolean validationForNegativeNumber(HttpServletRequest request) {
		boolean result = true;
		int currentValue = 0;
		for (int index = 0; index < storageOfBanknotes.getBanknotes().size(); index++) {
			currentValue = Integer.parseInt(request.getParameter(String.valueOf(index)));
			if (currentValue < 0) {
				result = false;
				return result;
			}
		}
		return result;
	}
	
	@Override
	protected boolean validationForInteger(HttpServletRequest request) {
		boolean result = false;
		String currentValue;
		for (int index = 0; index < storageOfBanknotes.getBanknotes().size(); index++) {
			currentValue = request.getParameter(String.valueOf(index));
			result = NumberUtils.isNumber(currentValue);
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	public boolean validationOfInputValues(HttpServletRequest request) {
		if (!(validationIsEmptyString(request))) {
			outputMessage = MESSAGE_VALIDATION_IS_EMPTY_STRING;
			return false;
		} else if (!(validationForInteger(request))) {
			outputMessage = MESSAGE_VALIDATION_FOR_INTEGER;
			return false;
		} else if (!(validationForNegativeNumber(request))) {
			outputMessage = MESSAGE_VALIDATION_NEGATIVE_NUMBER;
			return false;
		} else {
			return true;
		}
	}
	
	public String getOutputMessage() {
		return outputMessage;
	}

}
