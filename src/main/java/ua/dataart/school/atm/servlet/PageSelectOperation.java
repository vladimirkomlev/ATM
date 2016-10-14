package ua.dataart.school.atm.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.dataart.school.atm.domain.Banknote;
import ua.dataart.school.atm.operations.OperationOfBanknoteImpl;
import ua.dataart.school.atm.operations.ValidationOfInputValuesImpl;
import ua.dataart.school.atm.operations.ZipArchive;
import ua.dataart.school.atm.storage.BanknoteStorage;

@WebServlet("/selection")
public class PageSelectOperation extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(PageSelectOperation.class);
	private static final String ARCHIVE_PATH = "logs/";
	private static final String LOG_PATH = "atm";
	private static final String TRIM_PATH = "webapps";
	private static final String NAME_FILE_ARCHIVE = "atmArchive.zip";
	protected ServletContext servletContext;
	protected BanknoteStorage storageOfBanknotes;
	protected OperationOfBanknoteImpl operationOfBanknoteImpl;
	protected ValidationOfInputValuesImpl validationOfInputValuesImpl;

	@Override
	public void init() throws ServletException {
		servletContext = getServletContext();
		operationOfBanknoteImpl = (OperationOfBanknoteImpl) servletContext.getAttribute("operations");
		validationOfInputValuesImpl = new ValidationOfInputValuesImpl(operationOfBanknoteImpl.getCapacityOfStorage());
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/jsp/selection.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		}
		return;
	}

	private String getArchiveLogs() throws IOException {
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

	protected void getDataFromJsp(HttpServletRequest request) {
		getNewStorageOfBanknotes();
		for (int i = 0; i < storageOfBanknotes.getBanknotes().size(); i++) {
			storageOfBanknotes.getBanknotes().get(i).setCount(Integer.parseInt((request.getParameter(String.valueOf(i)))));
		}
	}

	protected void sendValuesToJsp(String message, boolean resultChoose, String jspPath, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("resultChoose", resultChoose);
		request.setAttribute("message", message);
		request.getRequestDispatcher(jspPath).forward(request, response);
	}

	protected void saveInformationInLog(String informationAboutTransaction) {
		List<Banknote> saveStorageBonknotes = operationOfBanknoteImpl.getSaveStorage();
		StringBuilder sbResult = new StringBuilder();
		sbResult.append(informationAboutTransaction);
		for (Banknote banknote : saveStorageBonknotes) {
			if (banknote.getCount() != 0) {
				sbResult.append(banknote.toString());
				sbResult.append(";");
			}
		}
		sbResult.deleteCharAt(sbResult.length() - 1);
		LOG.info(sbResult);
	}

	private void getNewStorageOfBanknotes() {
		try {
			storageOfBanknotes = operationOfBanknoteImpl.getCopyOfTheStorageOfBanknotes();
		} catch (CloneNotSupportedException e) {
			LOG.info("Exception when cloning a storageOfBanknotes object. " + e.fillInStackTrace());
		}
	}
}