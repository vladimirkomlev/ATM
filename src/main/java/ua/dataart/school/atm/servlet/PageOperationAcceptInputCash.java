package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/putcash")
public class PageOperationAcceptInputCash extends PageSelectOperation {

	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	// vova - IDE require create serialVersionUID
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PageOperationAcceptInputCash.class);
	private static final String JSP_PUTCASH_PATH = "WEB-INF/jsp/putcash.jsp";
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		request.getRequestDispatcher(JSP_PUTCASH_PATH).forward(request, response);
	}

	// TODO: 8/11/16 eugene - overcomplicated method, rework
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message;
		String informationTransaction = "Receipt in cash-";
		boolean resultChoose = true;
		boolean resultOfValidaionInputValues = validationOfInputValuesImpl.validationOfInputValues(request);

		if (!resultOfValidaionInputValues) {
			sendValuesToJsp(validationOfInputValuesImpl.getOutputMessage(), resultChoose, JSP_PUTCASH_PATH, request,
					response);
			return;
		}

		getDataFromJsp(request);		
		int inputSum = operationOfBanknoteImpl.getAmountFromInputStorageOfBanknotes(banknoteStorage.getBanknotes());
		int resultSum = 0;
		try {
			resultSum = operationOfBanknoteImpl.acceptInputCash(banknoteStorage);
		} catch (CloneNotSupportedException e) {
			LOG.info("The cloning operation failed. " + e.fillInStackTrace());
		}
		if (inputSum == 0) {
			message = "Entered amount should be greater than zero. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (resultSum == 0) {
			message = "Unfortunately we can not accept your input amount. The number of banknotes filled in that you specified.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (inputSum == resultSum) {
			try {
				operationOfBanknoteImpl.saveCurrentStorageInMemory();
				saveInformationInLog(informationTransaction);
			} catch (CloneNotSupportedException e) {
				LOG.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			message = "Credited with the amount " + resultSum;
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (inputSum > resultSum) {
			message = "Unfortunately we can not accept you have the amount " + inputSum
					+ " of these denominations of banknotes. We can take from you amount " + resultSum
					+ " from the following denominations of banknotes:";
			banknoteStorage.setBanknotes(operationOfBanknoteImpl.getSaveStorage());
			servletContext.setAttribute("resultSum", resultSum);
			servletContext.setAttribute("selectedOperation", 0);
			servletContext.setAttribute("informationTransaction", informationTransaction);
			sendValuesToJsp(message, resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		}
	}
}