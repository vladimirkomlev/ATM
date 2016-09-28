package ua.dataart.school.atm.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.OperationOfBanknote;
import ua.dataart.school.atm.storage.BanknoteStorage;

@WebServlet("/")
// TODO: 8/11/16 eugene - bad class name
// vova - AtmStart renamed on PageStart
public class PageStart extends HttpServlet {

	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	// vova - IDE require create serialVersionUID
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PageStart.class);
	protected ServletContext servletContext;
	protected BanknoteStorage banknoteStorage;
	protected OperationOfBanknote operationOfBanknote;
	
	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		try {
			operationOfBanknote = new OperationOfBanknote();
			banknoteStorage = operationOfBanknote.getCopyOfTheStorageOfBanknotes();
		} catch (IOException e) {
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		} catch (CloneNotSupportedException e) {
			LOG.info("Exception when cloning a storageOfBanknotes object. " + e.fillInStackTrace());
		}

		servletContext.setAttribute("operations", operationOfBanknote);
		servletContext.setAttribute("banknoteStorage", banknoteStorage);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/atm.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		doGet(request, response);
	}

}