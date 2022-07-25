package com.ISMM.admin.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {

	public static void saveFile(String uploadDir , String fileName, MultipartFile multipartFile) throws IOException {

//		Converts String Path name to URI
		Path uploadPath = Paths.get(uploadDir);
		
		//If upload path doesn't exist we persist an upload path
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		try(InputStream inputStream = multipartFile.getInputStream()) {
			//File path relative to the upload directory
			Path filePath = uploadPath.resolve(fileName);
			
			//Copy method accepts (InputStream to read from, Target for the file path, and an optional for how the copy
			//			Should be done)
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException ex) {
			throw new IOException("Could not save file: " + fileName , ex);
			
		}
	}
	
	public static void cleanDir (String dir) {
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if (!Files.isDirectory(file)) {
					try {
						Files.delete(file);						
					} catch (IOException ex) {
						System.out.println("Could not delete file: " + file);
					}
				}
			});
		} catch (IOException ex) {
			System.out.println("Could not list directory: " + dirPath);
		}
	}
}
