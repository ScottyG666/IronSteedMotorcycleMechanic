package com.ISMM.admin.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ISMM.common.domain.User;

public class UserCSVExporter {
	
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
	public void export( List<User> listToExport, HttpServletResponse response) throws IOException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
		String timeStamp = dateFormatter.format(new Date());
		String fileName = "users_" + timeStamp + ".csv";
		
		response.setContentType("text/csv");
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"User ID", "Email" , "First Name" , "Last Name" , "Roles" , "Enabled"};
		String[] fieldMapping = { "id" , "email", "firstName" , "lastName" , "userRoles" , "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listToExport) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
		
	}
}
