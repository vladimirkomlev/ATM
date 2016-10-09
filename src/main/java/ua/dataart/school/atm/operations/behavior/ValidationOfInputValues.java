package ua.dataart.school.atm.operations.behavior;

import javax.servlet.http.HttpServletRequest;

public abstract class ValidationOfInputValues {

	protected abstract boolean validationByEmptyString(HttpServletRequest request);
	protected abstract boolean validationByNegativeNumber(HttpServletRequest request);
	protected abstract boolean validationByInteger(HttpServletRequest request);
	protected abstract boolean validationByZero(HttpServletRequest request);
	
}
