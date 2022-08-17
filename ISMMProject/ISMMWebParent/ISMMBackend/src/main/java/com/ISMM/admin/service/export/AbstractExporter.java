package com.ISMM.admin.service.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	
	/**
	 * Takes a list of stored users within the database of ismmdb and exports them in to a
	 * file with the CSV format. The name of the file is the Date in which the file was 
	 * requested, in the format of <code>yyyy-MM-dd-HH-mm-ss</code>.
	 * @param listToExport
	 * 				The list pulled from the UserRepository containing all of the Users
	 * 				
	 * @param response
	 * @throws IOException 
	 */
	public void setResponseHeader( HttpServletResponse response, String contentType,
			String extension) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
		String timeStamp = dateFormatter.format(new Date());
		String fileName = "users_" + timeStamp + extension;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
	}
}
