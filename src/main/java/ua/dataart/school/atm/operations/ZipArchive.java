package ua.dataart.school.atm.operations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class ZipArchive {
	private static final Logger LOG = Logger.getLogger(ZipArchive.class);
	private List<String> fileList;
	private String outputZipFile;
	private String sourceFolder;
	private String nameZipFile = "atmArchive.zip";

	public ZipArchive(String sourceFolder, String outputZipFile) throws IOException {
		this.sourceFolder = sourceFolder;
		this.outputZipFile = outputZipFile + nameZipFile;
		fileList = new ArrayList<>();

		generateFileList(new File(this.sourceFolder));
		zipIt(this.outputZipFile);
	}

	public String getOutputZipFile() {
		return outputZipFile;
	}

	private void zipIt(String zipFile) throws IOException {

		byte[] buffer = new byte[1024];

		FileOutputStream outputStream;
		ZipOutputStream zipOutputStream = null;
		FileInputStream inputStream = null;
		try {
			outputStream = new FileOutputStream(zipFile);
			zipOutputStream = new ZipOutputStream(outputStream);
			System.out.println("Output to Zip: " + zipFile);
			for (String file : this.fileList) {
				System.out.println("File added: " + file);
				ZipEntry zipEntry = new ZipEntry(file);
				zipOutputStream.putNextEntry(zipEntry);
				inputStream = new FileInputStream(sourceFolder + File.separator + file);

				int len;
				while ((len = inputStream.read(buffer)) > 0) {
					zipOutputStream.write(buffer, 0, len);
				}
			}
			System.out.println("Done");
		} catch (IOException e) {
			LOG.info("File not found or damaged. " + e.fillInStackTrace());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (zipOutputStream != null) {
				zipOutputStream.closeEntry();
				zipOutputStream.close();
			}
		}
	}

	private void generateFileList(File file) {
		if (file.isFile()) {
			fileList.add(generateZipEntry(file.getAbsoluteFile().toString()));
		} else if (file.isDirectory()) {
			String[] subFile = file.list();
			for (String fileName : subFile) {
				generateFileList(new File(file, fileName));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(sourceFolder.length() + 1, file.length());
	}
}
