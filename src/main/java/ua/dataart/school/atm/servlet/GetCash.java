package ua.dataart.school.atm.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;

@WebServlet("/getcash")
// TODO: 8/11/16 eugene - bad class name
public class GetCash extends SelectionOperation {

	// TODO: 8/11/16 eugene - redundant empty javadoc
	/**
	 * 
	 */
	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	private static final long serialVersionUID = 1L;
	// TODO: 8/11/16 eugene - static final constants should be in uppercase
	private static final Logger log = Logger.getLogger(GetCash.class);
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: 8/11/16 eugene - why you call 'init' explicitly? Servlet container will call it
		init();
		request.getRequestDispatcher(JSP_GETCASH_PATH).forward(request, response);
	}

	// TODO: 8/11/16 eugene - overcomplicated method, rework
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String message;
		String informationTransaction = "Give out cash-";
		boolean resultChoose = true;
		Boolean resultValidatonForInteger;
		Boolean resultValidaionEmptyString;
		Boolean resultNegativeValue;

		resultValidaionEmptyString = validationEmptyString(request);
		if (resultValidaionEmptyString == false) {
			message = "You do not specify the amount of banknotes. Please, enter the amount of banknotes.";
			sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
			return;
		}

		resultValidatonForInteger = validationForInteger(request);
		if (resultValidatonForInteger == false) {
			message = "One of the fields is the value specified was not a number. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
			return;
		}

		resultNegativeValue = validationNegativeNumber(request);
		if (resultNegativeValue == false) {
			message = "One of the fields is the value specified negative number. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
			return;
		}

		List<Banknote> storage = getDataFromJsp(request);
		int requiredSum = operations.getSumFromBanknoteStorageInput(storage);
		banknoteStorage.setBanknotes(storage);
		int resultSum = 0;
		try {
			resultSum = operations.getCash(banknoteStorage);
		} catch (CloneNotSupportedException e) {
			log.info("The cloning operation failed. " + e.fillInStackTrace());
		}

		if (requiredSum == 0) {
			message = "Entered amount should be greater than zero. Please, enter the correct value.";
			sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
		} else if (resultSum == 0) {
			resultSum = operations.getCashWhenResultSumLessRequiredSum();
			if (resultSum == 0) {
				message = "Unfortunately the requested banknotes is missing in the ATM";
				sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
			} else {
				message = "Unfortunately we can not give you the required amount in the requested banknotes. We can give you sum "
						+ resultSum + " the following denominations of banknotes:";
				banknoteStorage.setBanknotes(operations.getSaveStorage());
				banknoteStorage.setBanknotes(operations.getSaveStorage());
				servletContext.setAttribute("resultSum", resultSum);
				servletContext.setAttribute("selectedOperation", 1);
				servletContext.setAttribute("informationTransaction", informationTransaction);
				sendValuesToJsp(message, resultChoose, JSP_ANOTHERSUM_PATH, request, response);
			}
		} else if (requiredSum == resultSum) {
			try {
				operations.saveCurrentStorageInMemory();
				saveInformationInLog(informationTransaction);
			} catch (CloneNotSupportedException e) {
				log.info("The cloning operation failed. " + e.fillInStackTrace());
			}
			message = "You got: " + resultSum;
			sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
		} else if (requiredSum > resultSum) {
			resultSum = operations.getCashWhenResultSumLessRequiredSum();
			message = "Unfortunately we can not give you the required amount in the requested banknotes. We can give you sum "
					+ resultSum + " the following denominations of banknotes:";
			banknoteStorage.setBanknotes(operations.getSaveStorage());
			servletContext.setAttribute("resultSum", resultSum);
			servletContext.setAttribute("selectedOperation", 1);
			servletContext.setAttribute("informationTransaction", informationTransaction);
			sendValuesToJsp(message, resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		}
	}
}