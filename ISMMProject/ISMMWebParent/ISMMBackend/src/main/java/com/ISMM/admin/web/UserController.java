package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.ISMM.admin.service.UserService;
import com.ISMM.common.domain.User;

@Controller
public class UserController {

	
	@Autowired 
	private UserService userService;
	
	@GetMapping("/users")
	public String listAll (ModelMap model) {
		List<User> listOfUsers = userService.listAll();
		model.put("listOfUsers", listOfUsers);
		
		return "users";
	}
	
	@GetMapping("/users/new")
	public String newUser() {
		return "user_form";
	}
}
