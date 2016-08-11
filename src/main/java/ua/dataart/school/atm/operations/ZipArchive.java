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

// TODO: 8/11/16 eugene - bad formatting
// TODO: 8/11/16 eugene - class has some design issues
public class ZipArchive {
	// TODO: 8/11/16 eugene - static final constants should be in uppercase
	private static final Logger log=Logger.getLogger(ZipArchive.class);
	// TODO: 8/11/16 eugene - make private
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

	// TODO: 8/11/16 eugene - may be private
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
			// TODO: 8/11/16 eugene - you never close streams if exception occurs. Use 'finally'
			zipOutputStream.closeEntry();
			zipOutputStream.close();
			System.out.println("Done");
		} catch (IOException e) {
			// TODO: 8/11/16 eugene - why do you log Throwable? Use e.getStackTrace()
			log.info("File not found or damaged. " + e.fillInStackTrace());;
		}
	}

	// TODO: 8/11/16 eugene - may be private
	public void generateFileList(File file) {
		if(file.isFile()){
			fileList.add(generateZipEntry(file.getAbsoluteFile().toString()));
		}
		// TODO: 8/11/16 eugene - file cant be dir and file at the same time. Use 'else'
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
	// TODO: 8/11/16 eugene - redundant linebreaks
	
	
}
