package com.ISMM.admin.users;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.exceptions.UserNotFoundException;
import com.ISMM.admin.service.FileUploadUtil;
import com.ISMM.admin.service.RoleService;
import com.ISMM.admin.service.export.UserCSVExporter;
import com.ISMM.admin.service.export.UserExcelExporter;
import com.ISMM.admin.service.export.UserPDFExporter;
import com.ISMM.common.domain.User;

@Controller
@RequestMapping("/users")
public class UserController {

	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private RoleService roleService;
	
	@GetMapping("")
	public String listFirstPage (ModelMap model) {
		return listByPage(1, model , "firstName" , "asc", null);
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") Integer pageNum, ModelMap model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<User> page = userService.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listOfUsers = page.getContent();
		long startCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
 		
		model.put("currentPage", pageNum);
		model.put("totalPages", page.getTotalPages());

		model.put("startCount", startCount);
		model.put("endCount", endCount);
		model.put("totalItems", page.getTotalElements());
		model.put("listOfUsers", listOfUsers);
		model.put("sortField", sortField);
		model.put("sortDir", sortDir);
		model.put("reverseSortDir", reverseSortDir);
		model.put("keyword", keyword);

		return "users/users";
	}
	
	@GetMapping("/new")
	public String newUser(ModelMap model) {
		
		User user = new User();
		user.setEnabled(true);
		
		model.put("user", user);
		model.put("listOfRoles", roleService.retreiveListOfRoles());
		model.put("pageTitle", "Create New User");
		
		return "users/user_form";
	}
	
	@PostMapping("/save")
	public String postUserForm(User user, RedirectAttributes redirectAttributes,
							    @RequestParam("image")MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.saveUser(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			
			userService.saveUser(user);			
		}
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
		
		return userService.getRedirectURLOfAffectedUser(user);
	}
	
	
	@PostMapping("/check_email")
	@ResponseBody
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
	
	@GetMapping("/edit/{id}" )
	public String editUser(@PathVariable(name = "id") Integer userId, ModelMap model, RedirectAttributes redirectAttributes) {
		try {
			model.put("user", userService.getById(userId));
			model.put("pageTitle", "Edit User (ID : " + userId + " )");
			model.put("listOfRoles", roleService.retreiveListOfRoles());

			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}	
	}
	
	@GetMapping("/delete/{id}" )
	public String deleteUser(@PathVariable(name = "id") Integer userId, ModelMap model, RedirectAttributes redirectAttributes) {
		
		String uploadDir = "user-photos/" + userId;
		FileUploadUtil.deleteDir(uploadDir);
		try {
			userService.delete(userId);
			redirectAttributes.addFlashAttribute("message" ,"The user ID " + userId + " has been deleted.");
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
		}		
		return "redirect:/users";
	}
		
	
	@GetMapping("/{id}/enabled/{status}")
	public String updateUserEnabledStatus ( @PathVariable("id") Integer id,
											@PathVariable("status") Boolean enabled, RedirectAttributes redirectAttributes) {
		userService.updateUserEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The user ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message" , message);
		
		return "redirect:/users";
	}
	
	
	@GetMapping("/export/csv")
	public void exportToCSV( HttpServletResponse response) throws IOException {
		List<User> listOfUsers = userService.listAll();
		UserCSVExporter exporter = new UserCSVExporter ();
		
		exporter.export(listOfUsers, response);
	}
	
	@GetMapping("/export/excel")
	public void exportToExcel( HttpServletResponse response) throws IOException {
		List<User> listOfUsers = userService.listAll();
		UserExcelExporter exporter = new UserExcelExporter ();
		
		exporter.export(listOfUsers, response);
	}
	
	@GetMapping("/export/pdf")
	public void exportToPDF( HttpServletResponse response) throws IOException {
		List<User> listOfUsers = userService.listAll();
		UserPDFExporter exporter = new UserPDFExporter ();
		
		exporter.export(listOfUsers, response);
	}
}
