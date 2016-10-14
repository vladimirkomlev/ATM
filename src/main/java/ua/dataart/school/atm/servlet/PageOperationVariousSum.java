package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.dataart.school.atm.operations.DifferentAmountForAcceptInputCash;
import ua.dataart.school.atm.operations.DifferentAmountForGiveRequiredAmount;
import ua.dataart.school.atm.operations.behavior.DifferentAmount;

@WebServlet("/anothersum")
public class PageOperationVariousSum extends PageSelectOperation {

	private static final long serialVersionUID = 1L;
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";
	private static final String JSP_PUTCASH_PATH = "WEB-INF/jsp/putcash.jsp";
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher(JSP_ANOTHERSUM_PATH).forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int selectedOperation = (Integer) servletContext.getAttribute("selectedOperation");
		StringBuilder sbSubmitted = new StringBuilder();
		sbSubmitted.append(request.getParameter("submitted"));
		int resultAmount = (int) servletContext.getAttribute("resultSum");

		if (selectedOperation == 1) {
			DifferentAmount dfForRequiredAmount = new DifferentAmountForGiveRequiredAmount(operationOfBanknoteImpl,
					resultAmount);
			dfForRequiredAmount.selectAnotherAmount(request);
			sendValuesToJsp(dfForRequiredAmount.getOutputMessage(), dfForRequiredAmount.getResultChoose(), JSP_GETCASH_PATH,
					request, response);
			return;
		} else if (selectedOperation == 0) {
			DifferentAmount dfAcceptInputCash = new DifferentAmountForAcceptInputCash(operationOfBanknoteImpl, resultAmount);
			dfAcceptInputCash.selectAnotherAmount(request);
			sendValuesToJsp(dfAcceptInputCash.getOutputMessage(), dfAcceptInputCash.getResultChoose(), JSP_PUTCASH_PATH,
					request, response);
			return;
		}

	}
}