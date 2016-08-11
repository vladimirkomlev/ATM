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
// TODO: 8/11/16 eugene - bad class name
public class AtmStart extends HttpServlet {

	// TODO: 8/11/16 eugene - redundant empty javadoc
	/**
	 *
	 */
	// TODO: 8/11/16 eugene - you don't need serialVersionUID
	private static final long serialVersionUID = 1L;
	// TODO: 8/11/16 eugene - static final constants should be in uppercase
	private static final Logger log = Logger.getLogger(AtmStart.class);
	private final String BANKNOTES_PROPERTIES_PATH = "WEB-INF/classes/banknote.properties";
	protected ServletContext servletContext;
	protected BanknoteStorage banknoteStorage;
	protected Operations operations;
	protected StringBuilder realPath;

	// TODO: 8/11/16 eugene - add @Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		try {
			// TODO: 8/11/16 eugene - this is strange and messy
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
			// TODO: 8/11/16 eugene - redundant commented code
//			log.info("inputStream available="+inputStream.available());
		} catch (IOException e) {
			log.info("File not found or damaged. " + e.fillInStackTrace());
		}

		// TODO: 8/11/16 eugene - you never use "realPath"
		servletContext.setAttribute("realPath", realPath);
		servletContext.setAttribute("operations", operations);
		servletContext.setAttribute("banknoteStorage", banknoteStorage);
	}

	// TODO: 8/11/16 eugene - add @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/atm.jsp").forward(request, response);
	}

	// TODO: 8/11/16 eugene - add @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		doGet(request, response);
	}

}