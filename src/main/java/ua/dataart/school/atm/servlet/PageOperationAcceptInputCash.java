package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.InputAmountForAcceptInputCash;
import ua.dataart.school.atm.operations.behavior.RequiredAmount;

@WebServlet("/putcash")
public class PageOperationAcceptInputCash extends PageSelectOperation {

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

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String informationTransaction = "Receipt in cash-";
		boolean resultChoose = true;
		boolean resultOfValidaionInputValues = validationOfInputValuesImpl.validationOfInputValues(request);

		if (!resultOfValidaionInputValues) {
			sendValuesToJsp(validationOfInputValuesImpl.getOutputMessage(), resultChoose, JSP_PUTCASH_PATH, request,
					response);
			return;
		}

		getDataFromJsp(request);
		RequiredAmount inputRequiredAmount = new  InputAmountForAcceptInputCash(banknoteStorage, operationOfBanknoteImpl);
		boolean resultInputRequitedAmount = inputRequiredAmount.searchOfTheRequiredAmount();
		
		if(!resultInputRequitedAmount){
			servletContext.setAttribute("resultSum", inputRequiredAmount.getResultAmount());
			servletContext.setAttribute("selectedOperation", 0);
			servletContext.setAttribute("informationTransaction", informationTransaction);
			sendValuesToJsp(inputRequiredAmount.getOutputMessage(), resultChoose, JSP_ANOTHERSUM_PATH, request, response);
		}else{
			sendValuesToJsp(inputRequiredAmount.getOutputMessage(), resultChoose, JSP_PUTCASH_PATH, request, response);
		}
	}
}