package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
		
		model.put("newUser", new User());
		model.put("listOfRoles", roleService.retreiveListOfRoles());
		
		return "user_form";
	}
	
	@PostMapping("/users/save")
	public String postNewUserForm(User user) {
		System.out.println(user);
		
		return "redirect:/users";
	}
}
