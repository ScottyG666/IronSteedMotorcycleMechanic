package com.ISMM.admin.service.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.ISMM.common.domain.User;

public class UserCSVExporter extends AbstractExporter{
	
	
	 public void export( List<User> listToExport, HttpServletResponse response) throws IOException {
		
		 super.setResponseHeader(response, "text/csv", ".csv");
		 
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
