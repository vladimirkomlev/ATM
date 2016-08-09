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

	private static final Logger log=Logger.getLogger(ZipArchive.class);
	List<String> fileList;
	private String outputZipFile;
	private String sourceFolder;
	private String nameZipFile="atmArchive.zip";
	
	public ZipArchive(String sourceFolder, String outputZipFile){
		this.sourceFolder=sourceFolder;
		this.outputZipFile=outputZipFile+nameZipFile;
		fileList=new ArrayList<>();
		
		generateFileList(new File(this.sourceFolder));
		zipIt(this.outputZipFile);
	}
	
	public String getOutputZipFile(){
		return outputZipFile;
	}
	
	public void zipIt(String zipFile) {
		
		byte[] buffer=new byte[1024];
		
		try {
			FileOutputStream outputStream=new FileOutputStream(zipFile);
			ZipOutputStream zipOutputStream=new ZipOutputStream(outputStream);
			System.out.println("Output to Zip: "+zipFile);
			for (String file : this.fileList) {
				System.out.println("File added: "+file);
				ZipEntry zipEntry=new ZipEntry(file);
				zipOutputStream.putNextEntry(zipEntry);
				FileInputStream inputStream=new FileInputStream(sourceFolder + File.separator + file);
				
				int len;
				while ((len=inputStream.read(buffer))>0) {
					zipOutputStream.write(buffer, 0, len);
				}
				inputStream.close();
			}
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			System.out.println("Done");
		} catch (IOException e) {
			log.info("File not found or damaged. " + e.fillInStackTrace());;
		}
	}
	
	public void generateFileList(File file) {
		if(file.isFile()){
			fileList.add(generateZipEntry(file.getAbsoluteFile().toString()));
		}
		if(file.isDirectory()){
			String[]subFile=file.list();
			for (String fileName : subFile) {
				generateFileList(new File(file,fileName));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(sourceFolder.length()+1,file.length());
	}
	
	
	
}
