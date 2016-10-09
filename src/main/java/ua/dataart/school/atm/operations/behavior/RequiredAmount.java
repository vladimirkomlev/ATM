package ua.dataart.school.atm.operations.behavior;

import java.io.IOException;

public abstract class RequiredAmount {
	
	protected static final String MESSAGE_INPUT_ZERO = "Entered amount should be greater than zero. Please, enter the correct value.";
	protected static final String MESSAGE_AMOUNT_IS_MISSING = "Unfortunately the required banknotes is missing in the ATM.";
	protected static final String MESSAGE_ANOTHER_REQUSTED_SUM_PATH_FIRST = "Unfortunately we can not give you the required amount in the requested banknotes. We can give you sum ";
	protected static final String MESSAGE_ANOTHER_REQUSTED_SUM_PATH_SECOND = " the following denominations of banknotes:";
	protected static final String MESSAGE_YOU_GOT = "You got: ";
	protected static final String MESSAGE_BANKNOTE_IS_FULL = "Unfortunately we can not accept your input amount. The number of banknotes filled in that you specified.";
	protected static final String MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_FIRST = "Unfortunately we can not accept you have the amount ";
	protected static final String MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_SECOND = " of these denominations of banknotes. We can take from you amount ";
	protected static final String MESSAGE_ANOTHER_ACCEPTED_SUM_PATH_THIRD = " from the following denominations of banknotes:";
	protected static final String MESSAGE_CDERITED_AMOUNT = "Credited with the amount ";
	
	public abstract boolean searchOfTheRequiredAmount() throws IOException;
	public abstract String getOutputMessage();
	public abstract int getResultAmount();
	protected abstract void initRequiredAndResultAmount() throws IOException;
	

}
