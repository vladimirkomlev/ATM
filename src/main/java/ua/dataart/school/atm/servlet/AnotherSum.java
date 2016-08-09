package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/anothersum")
public class AnotherSum extends SelectionOperation {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AnotherSum.class);
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";
	private static final String JSP_PUTCASH_PATH = "WEB-INF/jsp/putcash.jsp";
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		request.getRequestDispatcher(JSP_ANOTHERSUM_PATH).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		String strConfirm = "Confirm";
		String strCancel = "Cancel";
		int selectedOperation = (Integer) servletContext.getAttribute("selectedOperation");
		StringBuilder sbSubmitted = new StringBuilder();
		sbSubmitted.append(request.getParameter("submitted"));
		boolean resultChoose = true;
		String message = "";
		int result = (int) servletContext.getAttribute("resultSum");
		if (selectedOperation == 1) {
			if (sbSubmitted.length() == strConfirm.length()) {
				try {
					operations.saveCurrentStorageInMemory();
					saveInformationInLog(servletContext.getAttribute("informationTransaction").toString());
				} catch (CloneNotSupportedException e) {
					log.info("The cloning operation failed. " + e.fillInStackTrace());
				}
				message = "You got: " + result;
				sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
				return;
			} else if (sbSubmitted.length() == strCancel.length()) {
				resultChoose = false;
				sendValuesToJsp(message, resultChoose, JSP_GETCASH_PATH, request, response);
				return;
			}

		} else if (selectedOperation == 0) {
			if (sbSubmitted.length() == strConfirm.length()) {
				try {
					operations.saveCurrentStorageInMemory();
					saveInformationInLog(servletContext.getAttribute("informationTransaction").toString());
				} catch (CloneNotSupportedException e) {
					log.info("The cloning operation failed. " + e.fillInStackTrace());
				}
				message = "Credited with the amount " + result;
				sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
				return;
			} else if (sbSubmitted.length() == strCancel.length()) {
				resultChoose = false;
				sendValuesToJsp(message, resultChoose, JSP_PUTCASH_PATH, request, response);
				return;
			}
		}
	}
}