package ua.dataart.school.atm.operations;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.behavior.DifferentAmount;

public class DifferentAmountForAcceptInputCash extends DifferentAmount {

	private static final Logger LOG = Logger.getLogger(DifferentAmountForAcceptInputCash.class);
	private static final String MESSAGE_CREDITED_AMOUNT = "Credited with the amount ";
	private static final String INFO_TRANSACTION = "Receipt in cash-";

	private StringBuilder sbSubmitted;
	private OperationOfBanknoteImpl operationOfBanknote;
	private int resultAmount;
	private String outputMessage;
	private boolean resultChoose;

	public DifferentAmountForAcceptInputCash(OperationOfBanknoteImpl operationOfBanknote, int resultAmount) {
		this.operationOfBanknote = operationOfBanknote;
		this.resultAmount = resultAmount;
	}

	@Override
	public boolean selectAnotherAmount(HttpServletRequest request) {
		sbSubmitted = new StringBuilder();
		sbSubmitted.append(request.getParameter("submitted"));
		if (!equalsWithConfirm()) {
			return true;
		} else {
			return equalsWithCancel();
		}
	}

	private boolean equalsWithConfirm() {
		if (sbSubmitted.length() == CONFIRM.length()) {
			try {
				operationOfBanknote.saveCurrentStorageInMemory();
				saveInfoTransaction(operationOfBanknote.getSaveStorage(), INFO_TRANSACTION);
				outputMessage = MESSAGE_CREDITED_AMOUNT + resultAmount;
				resultChoose = true;
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean equalsWithCancel() {
		if (sbSubmitted.length() == CANCEL.length()) {
			outputMessage = MESSAGE_EMPTY;
			resultChoose = false;
			return false;
		} else {
			return true;
		}
	}

	@Override
	public String getOutputMessage() {
		return outputMessage;
	}

	@Override
	public boolean getResultChoose() {
		return resultChoose;
	}

}
