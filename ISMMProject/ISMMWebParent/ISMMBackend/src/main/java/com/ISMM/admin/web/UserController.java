package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.service.RoleService;
import com.ISMM.admin.service.UserService;
import com.ISMM.common.domain.User;

@Controller
public class UserController {

	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private RoleService roleService;
	
	@GetMapping("/users")
	public String listAll (ModelMap model) {
		List<User> listOfUsers = userService.listAll();
		model.put("listOfUsers", listOfUsers);
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser(ModelMap model) {
		
		User user = new User();
		user.setEnabled(true);
		
		model.put("newUser", user);
		model.put("listOfRoles", roleService.retreiveListOfRoles());
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String postNewUserForm(User user, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		userService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
		return "redirect:/users";
	}
	
	@ResponseBody
	@PostMapping("/users/check_email")
	public String checkDuplicateEmail(@Param("email") String email) {
			
		return userService.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
