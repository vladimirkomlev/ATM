package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

@WebServlet("/anothersum")
// TODO: 8/11/16 eugene - bad class name
public class AnotherSum extends SelectionOperation {

	// TODO: 8/11/16 eugene - redundant empty javadoc
	/**
	 * 
	 */
	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	private static final long serialVersionUID = 1L;
	// TODO: 8/11/16 eugene - static final constants should be in uppercase
	private static final Logger log = Logger.getLogger(AnotherSum.class);
	private static final String JSP_ANOTHERSUM_PATH = "WEB-INF/jsp/anothersum.jsp";
	private static final String JSP_PUTCASH_PATH = "WEB-INF/jsp/putcash.jsp";
	private static final String JSP_GETCASH_PATH = "WEB-INF/jsp/getcash.jsp";

	// TODO: 8/11/16 eugene - add @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: 8/11/16 eugene - why you call 'init' explicitly? Servlet container will call it
		init();
		request.getRequestDispatcher(JSP_ANOTHERSUM_PATH).forward(request, response);
	}

	// TODO: 8/11/16 eugene - overcomplicated method
	// TODO: 8/11/16 eugene - add @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO: 8/11/16 eugene - why you call 'init' explicitly? Servlet container will call it
		init();
		String strConfirm = "Confirm";
		String strCancel = "Cancel";
		int selectedOperation = (Integer) servletContext.getAttribute("selectedOperation");
		StringBuilder sbSubmitted = new StringBuilder();
		sbSubmitted.append(request.getParameter("submitted"));
		boolean resultChoose = true;
		String message = "";
		int result = (int) servletContext.getAttribute("resultSum");
		// TODO: 8/11/16 eugene - use some constants for operation codes
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