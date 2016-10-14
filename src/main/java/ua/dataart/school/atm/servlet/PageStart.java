package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.OperationOfBanknoteImpl;
import ua.dataart.school.atm.storage.BanknoteStorage;

@WebServlet("/")
public class PageStart extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PageStart.class);
	protected ServletContext servletContext;
	protected BanknoteStorage banknoteStorage;
	protected OperationOfBanknoteImpl operationOfBanknoteImpl;

	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		try {
			operationOfBanknoteImpl = new OperationOfBanknoteImpl();
		} catch (IOException e) {
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		} 
		servletContext.setAttribute("operations", operationOfBanknoteImpl);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/atm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		init();
		doGet(request, response);
	}

}