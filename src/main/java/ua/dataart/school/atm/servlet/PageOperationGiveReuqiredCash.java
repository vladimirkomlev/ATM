package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.dataart.school.atm.operations.RequiredAmountForGiveRequiredCash;
import ua.dataart.school.atm.operations.behavior.RequiredAmount;

@WebServlet("/getcash")
public class PageOperationGiveReuqiredCash extends PageSelectOperation {

	private static final long serialVersionUID = 1L;
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(JSP_GETCASH_PATH).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean resultChoose = true;
		boolean resultOfValidationInputValues = validationOfInputValuesImpl.validationOfInputValues(request);

		if (!resultOfValidationInputValues) {
			sendValuesToJsp(validationOfInputValuesImpl.getOutputMessage(), resultChoose, JSP_GETCASH_PATH, request,
					response);
			return;
		}

		getDataFromJsp(request);
		RequiredAmount requiredAmount = new RequiredAmountForGiveRequiredCash(storageOfBanknotes, operationOfBanknoteImpl);
		boolean resultRequiredAmount = requiredAmount.searchOfTheRequiredAmount();

		if (!resultRequiredAmount) {
			servletContext.setAttribute("resultSum", requiredAmount.getResultAmount());
			servletContext.setAttribute("banknoteStorage", storageOfBanknotes);
			servletContext.setAttribute("selectedOperation", 1);
			sendValuesToJsp(requiredAmount.getOutputMessage(), resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		} else {
			sendValuesToJsp(requiredAmount.getOutputMessage(), resultChoose, JSP_GETCASH_PATH, request, response);
		}
	}
}