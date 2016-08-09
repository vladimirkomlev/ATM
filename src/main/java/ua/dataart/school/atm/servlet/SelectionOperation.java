package ua.dataart.school.atm.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NumberUtils;
import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.Operations;
import ua.dataart.school.atm.operations.ZipArchive;
import ua.dataart.school.atm.storage.BanknoteStorage;

@WebServlet("/selection")
public class SelectionOperation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(SelectionOperation.class);
	private static final String ARCHIVE_PATH = "logs/";
	private static final String LOG_PATH = "atm";
	private static final String TRIM_PATH = "webapps";
	private static final String NAME_FILE_ARCHIVE = "atmArchive.zip";
	protected ServletContext servletContext;
	protected BanknoteStorage banknoteStorage;
	protected Operations operations;
	protected Properties properties;
//	protected StringBuilder realPath;

	public void init() throws ServletException {
		servletContext = getServletContext();
		operations = (Operations) servletContext.getAttribute("operations");
		banknoteStorage = (BanknoteStorage) servletContext.getAttribute("banknoteStorage");
//		realPath = (StringBuilder) servletContext.getAttribute("realPath");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		init();
		request.getRequestDispatcher("WEB-INF/jsp/selection.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pathToArchive = getArchiveLogs();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment;filename=atmArchive.zip");
		try {
			File file = new File(pathToArchive);
			FileInputStream inputStream = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();

			byte[] outputByte = new byte[2048];
			while (inputStream.read(outputByte, 0, 2048) != -1) {
				out.write(outputByte, 0, 2048);
			}
			inputStream.close();
			out.flush();
			out.close();
		} catch (IOException e) {
			log.info("File not found or damaged. " + e.fillInStackTrace());
		}
		return;
	}

	private String getArchiveLogs() {
		StringBuilder realPath = new StringBuilder();
		realPath.append(servletContext.getRealPath("/"));
		String pathToArchive;
		int indexTrim = 0;
		if (realPath.indexOf(TRIM_PATH) != -1) {
			indexTrim = realPath.indexOf(TRIM_PATH);
		}
		realPath.setLength(indexTrim);
		for (int index = 0; index < realPath.length(); index++) {
			if (realPath.charAt(index) == '\\') {
				realPath.setCharAt(index, '/');
			}
		}
		String outputZipFile = realPath.append(ARCHIVE_PATH).toString();
		String sourceFolder = realPath.append(LOG_PATH).toString();
		String outputZipFileWithName = outputZipFile + NAME_FILE_ARCHIVE;
		File file = new File(outputZipFileWithName);
		if (file.exists()) {
			file.delete();
			ZipArchive zipArchive = new ZipArchive(sourceFolder, outputZipFile);
			pathToArchive = zipArchive.getOutputZipFile();
			return pathToArchive;
		} else {
			ZipArchive zipArchive = new ZipArchive(sourceFolder, outputZipFile);
			pathToArchive = zipArchive.getOutputZipFile();
			return pathToArchive;
		}
	}

	protected List<Banknote> getDataFromJsp(HttpServletRequest request) {
		List<Banknote> storage = new BanknoteStorage().getBanknotes();
		Integer index = 0;
		for (Banknote banknote : storage) {
			if (index == 0) {
				banknote.setValue(500);
				banknote.setCount(Integer.parseInt(request.getParameter(index.toString())));
			} else if (index == 1) {
				banknote.setValue(200);
				banknote.setCount(Integer.parseInt(request.getParameter(index.toString())));
			} else if (index == 2) {
				banknote.setValue(100);
				banknote.setCount(Integer.parseInt(request.getParameter(index.toString())));
			} else if (index == 3) {
				banknote.setValue(50);
				banknote.setCount(Integer.parseInt(request.getParameter(index.toString())));
			} else if (index == 4) {
				banknote.setValue(20);
				banknote.setCount(Integer.parseInt(request.getParameter(index.toString())));
			}
			index++;
		}

		return storage;
	}

	protected void sendValuesToJsp(String message, boolean resultChoose, String jspPath, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("resultChoose", resultChoose);
		request.setAttribute("message", message);
		request.getRequestDispatcher(jspPath).forward(request, response);
	}

	protected void saveInformationInLog(String informationAboutTransaction) {
		List<Banknote> saveStorageBonknotes = operations.getSaveStorage();
		StringBuilder sbResult = new StringBuilder();
		sbResult.append(informationAboutTransaction);
		for (Banknote banknote : saveStorageBonknotes) {
			if (banknote.getCount() != 0) {
				sbResult.append(banknote.toString());
				sbResult.append(";");
			}
		}
		sbResult.deleteCharAt(sbResult.length() - 1);
		log.info(sbResult);
	}

	protected Boolean validationForInteger(HttpServletRequest request) {
		Boolean result = true;
		String currentValue;
		for (Integer index = 0; index < 5; index++) {
			currentValue = request.getParameter(index.toString());
			result = NumberUtils.isNumber(currentValue);
			if (result == false) {
				break;
			}

		}
		return result;

	}

	protected Boolean validationNegativeNumber(HttpServletRequest request) {
		Boolean result = true;
		Integer currentValue = 0;
		for (Integer index = 0; index < 5; index++) {
			currentValue = Integer.parseInt(request.getParameter(index.toString()));
			if (currentValue < 0) {
				result = false;
				break;
			}
		}
		return result;
	}

	protected Boolean validationEmptyString(HttpServletRequest request) {
		Boolean result = true;
		Integer index = 0;
		for (Integer i = 0; i < 5; i++) {
			if (request.getParameter(i.toString()) == "") {
				index++;
			}
		}
		if (index == 5) {
			result = false;
			return result;
		} else {
			return result;
		}
	}

}