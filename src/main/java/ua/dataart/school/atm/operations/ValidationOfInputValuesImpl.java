package ua.dataart.school.atm.operations;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NumberUtils;
import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.behavior.ValidationOfInputValues;
import ua.dataart.school.atm.storage.BanknoteStorage;

public class ValidationOfInputValuesImpl extends ValidationOfInputValues{
	
	private static final String MESSAGE_VALIDATION_IS_EMPTY_STRING = "You do not specify the amount of banknotes. Please, enter the amount of banknotes.";
	private static final String MESSAGE_VALIDATION_NEGATIVE_NUMBER = "One of the fields is the value specified negative number. Please, enter the correct value.";
	private static final String MESSAGE_VALIDATION_FOR_INTEGER = "One of the fields is the value specified was not a number. Please, enter the correct value.";
	private static final String MESSAGE_BY_ZERO = "Entered amount should be greater than zero. Please, enter the correct value.";
	private static final Logger LOG=Logger.getLogger(ValidationOfInputValuesImpl.class);

	private String outputMessage;
	private int sizeStorage;

	public ValidationOfInputValuesImpl(int capacityOfStorage) {
		this.sizeStorage = capacityOfStorage;
	}
	
	@Override
	protected boolean validationByZero(HttpServletRequest request) {
		int resultOfInputValues = 0;
		for (int index = 0; index < sizeStorage; index++) {
			resultOfInputValues += Integer.parseInt(request.getParameter(String.valueOf(index)));
		}
		LOG.info("intRes=" + resultOfInputValues);
		if (resultOfInputValues == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean validationByEmptyString(HttpServletRequest request) {
		int counter = 0;
		for (int index = 0; index < sizeStorage; index++) {
			if (request.getParameter(String.valueOf(index)) == "") {
				counter++;
			}
		}
		if (counter == sizeStorage) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected boolean validationByNegativeNumber(HttpServletRequest request) {
		int currentValue = 0;
		for (int index = 0; index < sizeStorage; index++) {
			currentValue = Integer.parseInt(request.getParameter(String.valueOf(index)));
			if (currentValue < 0) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected boolean validationByInteger(HttpServletRequest request) {
		boolean result = true;
		String currentValue;
		for (int index = 0; index < sizeStorage; index++) {
			currentValue = request.getParameter(String.valueOf(index));
			result = NumberUtils.isNumber(currentValue);
			if (!result) {
				result=false;
				break;
			}
		}
		return result;
	}
	
	public boolean validationOfInputValues(HttpServletRequest request) {
		if (!(validationByEmptyString(request))) {
			outputMessage = MESSAGE_VALIDATION_IS_EMPTY_STRING;
			return false;
		} else if (!(validationByInteger(request))) {
			outputMessage = MESSAGE_VALIDATION_FOR_INTEGER;
			return false;
		} else if (!(validationByNegativeNumber(request))) {
			outputMessage = MESSAGE_VALIDATION_NEGATIVE_NUMBER;
			return false;
		} else if (!(validationByZero(request))) {
			outputMessage = MESSAGE_BY_ZERO;
			return false;
		} else {
			return true;
		}
	}
	
	public String getOutputMessage() {
		return outputMessage;
	}

}
