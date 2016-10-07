package ua.dataart.school.atm.operations.behavior;

import javax.servlet.http.HttpServletRequest;

public abstract class ValidationOfInputValues {

	protected abstract boolean validationIsEmptyString(HttpServletRequest request);
	protected abstract boolean validationForNegativeNumber(HttpServletRequest request);
	protected abstract boolean validationForInteger(HttpServletRequest request);
	
}
