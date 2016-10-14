package ua.dataart.school.atm.operations.behavior;

import javax.servlet.http.HttpServletRequest;

public abstract class DifferentAmount {
	
	protected static final String CONFIRM = "Confirm";
	protected static final String CANCEL = "Cancel";
	protected static final String MESSAGE_EMPTY="";
	
	public abstract boolean selectAnotherAmount(HttpServletRequest request);
	public abstract String getOutputMessage();
	public abstract boolean getResultChoose();

}
