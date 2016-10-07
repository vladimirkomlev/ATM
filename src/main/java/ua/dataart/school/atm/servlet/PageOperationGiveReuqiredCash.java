package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.SearchAmountInTheStorage;

@WebServlet("/getcash")
public class PageOperationGiveReuqiredCash extends PageSelectOperation {

	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	// vova - IDE require create serialVersionUID
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PageOperationGiveReuqiredCash.class);
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: 8/11/16 eugene - why you call 'init' explicitly? Servlet container will call it
		// vova - Servlet container call method "init" when create instance of class.
		// While instance of class already exists and I call methods "doGet" or "doPost then method "init" does not call.
		// That is why I call method "init" explicitly.
		init();
		request.getRequestDispatcher(JSP_GETCASH_PATH).forward(request, response);
	}

	// TODO: 8/11/16 eugene - overcomplicated method, rework
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String informationTransaction = "Give out cash-";
		boolean resultChoose = true;
		boolean resultOfValidationInputValues = validationOfInputValuesImpl.validationOfInputValues(request);
		
		if (!resultOfValidationInputValues) {
			LOG.info("OutputMessage: " + validationOfInputValuesImpl.getOutputMessage());
			sendValuesToJsp(validationOfInputValuesImpl.getOutputMessage(), resultChoose, JSP_GETCASH_PATH, request,
					response);
			return;
		}

		getDataFromJsp(request);
		SearchAmountInTheStorage searchAmount = new SearchAmountInTheStorage(banknoteStorage, operationOfBanknoteImpl);
		boolean resultSearchMethod = searchAmount.searchOfTheRequiredAmount();

		if (!resultSearchMethod) {
			servletContext.setAttribute("resultSum", searchAmount.getResultSum());
			servletContext.setAttribute("selectedOperation", 1);
			servletContext.setAttribute("informationTransaction", informationTransaction);
			sendValuesToJsp(searchAmount.getOutputMassage(), resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		} else {
			LOG.info("resultSearchMethod=" + resultSearchMethod);
			sendValuesToJsp(searchAmount.getOutputMassage(), resultChoose, JSP_GETCASH_PATH, request, response);
		}
	}
}