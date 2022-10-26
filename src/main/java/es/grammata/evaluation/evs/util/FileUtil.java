package es.grammata.evaluation.evs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class FileUtil {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(FileUtil.class);

	public static FileInputStream loadFile(String path, String fileName) {
		File file = new File(System.getProperty("catalina.home") + File.separator + path + fileName);

		FileInputStream fin = null;

		int ch;
		StringBuffer sb = new StringBuffer();

		try {
			fin = new FileInputStream(file);
			while ((ch = fin.read()) != -1) {
				sb.append((char) ch);
			}
		} catch (FileNotFoundException e) {
			log.error("Fichero no encontrado: " + e);
		} catch (IOException ioe) {
			log.error("Error leyendo fichero: " + ioe);
		} 
		
		
		return fin;
	}
	
	
	public static File createDir(String path) {
		File dir = new File(path);
		if (!dir.exists())		
			dir.mkdirs();
		
		return dir;
	}
	
	public static void deleteFile(String path) {
		File fileTmp = new File(path);
		if(fileTmp != null) {
			fileTmp.delete();
		}
	}
	
	public static void createZipfile(String zipFileName, List<String> files, String path) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(zipFileName);
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		for(String fileName : files) {
			addFileToZip(fileName, zos, path)	;
		}
		
		zos.close();
	}
	
	private static void addFileToZip(String fileName, ZipOutputStream zos, String path) throws FileNotFoundException, IOException {
		File file = new File(path + fileName);
		FileInputStream fis = new FileInputStream(file);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

}
