package ua.dataart.school.atm.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;

@WebServlet("/putcash")
public class PutCash extends SelectionOperation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(PutCash.class);
	private static final String JSP_PUTCASH_PATH = "WEB-INF/jsp/putcash.jsp";
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		request.getRequestDispatcher(JSP_PUTCASH_PATH).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message;
		String informationTransaction = "Receipt in cash-";
		boolean resultChoose = true;
		Boolean resultValidatonForInteger;
		Boolean resultValidaionEmptyString;
		Boolean resultNegativeValue;

		resultValidaionEmptyString = validationEmptyString(request);
		if (resultValidaionEmptyString == false) {
			message = "You do not specify the amount of banknotes. Please, enter the amount of banknotes.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
			return;
		}

		resultValidatonForInteger = validationForInteger(request);
		if (resultValidatonForInteger == false) {
			message = "One of the fields is the value specified was not a number. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
			return;
		}

		resultNegativeValue = validationNegativeNumber(request);
		if (resultNegativeValue == false) {
			message = "One of the fields is the value specified negative number. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
			return;
		}

		List<Banknote> storage = getDataFromJsp(request);
		int inputSum = operations.getSumFromBanknoteStorageInput(storage);
		banknoteStorage.setBanknotes(storage);
		int resultSum = 0;
		try {
			resultSum = operations.putCash(banknoteStorage);
		} catch (CloneNotSupportedException e) {
			log.info("The cloning operation failed. " + e.fillInStackTrace());
		}
		if (inputSum == 0) {
			message = "Entered amount should be greater than zero. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (resultSum == 0) {
			message = "Unfortunately we can not accept your input amount. The number of banknotes filled in that you specified.";
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (inputSum == resultSum) {
			try {
				operations.saveCurrentStorageInMemory();
				saveInformationInLog(informationTransaction);
			} catch (CloneNotSupportedException e) {
				log.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			message = "Credited with the amount " + resultSum;
			sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
		} else if (inputSum > resultSum) {
			message = "Unfortunately we can not accept you have the amount " + inputSum
					+ " of these denominations of banknotes. We can take from you amount " + resultSum
					+ " from the following denominations of banknotes:";
			banknoteStorage.setBanknotes(operations.getSaveStorage());
			servletContext.setAttribute("resultSum", resultSum);
			servletContext.setAttribute("selectedOperation", 0);
			servletContext.setAttribute("informationTransaction", informationTransaction);
			sendValuesToJsp(message, resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		}
	}
}