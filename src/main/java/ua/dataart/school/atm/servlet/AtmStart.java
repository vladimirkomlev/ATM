package ua.dataart.school.atm.servlet;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.operations.Operations;
import ua.dataart.school.atm.storage.BanknoteStorage;

@WebServlet("/")
public class AtmStart extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(AtmStart.class);
	private final String BANKNOTES_PROPERTIES_PATH = "WEB-INF/classes/banknote.properties";
	protected ServletContext servletContext;
	protected BanknoteStorage banknoteStorage;
	protected Operations operations;
	protected StringBuilder realPath;

	public void init() throws ServletException {
		servletContext = getServletContext();
		try {
			realPath = new StringBuilder();
			ClassLoader classLoader=Thread.currentThread().getContextClassLoader();
			InputStream inputStream = classLoader.getResourceAsStream("banknote.properties");
			
			log.info("inputStream available="+inputStream.available());
			realPath.append(servletContext.getRealPath(BANKNOTES_PROPERTIES_PATH));
			operations = new Operations(inputStream);
			log.info("operation FilePath available="+operations.getFilePath().available());
			banknoteStorage = new BanknoteStorage();
			log.info("realPath / ="+servletContext.getRealPath("/"));
			log.info("realPath="+realPath);
			inputStream.close();
//			log.info("inputStream available="+inputStream.available());
		} catch (IOException e) {
			log.info("File not found or damaged. " + e.fillInStackTrace());
		}
		servletContext.setAttribute("realPath", realPath);
		servletContext.setAttribute("operations", operations);
		servletContext.setAttribute("banknoteStorage", banknoteStorage);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/atm.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		doGet(request, response);
	}

}