package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.service.RoleService;
import com.ISMM.admin.service.UserNotFoundException;
import com.ISMM.admin.service.UserService;
import com.ISMM.common.domain.User;

@Controller
@RequestMapping("/users")
public class UserController {

	
	@Autowired 
	private UserService userService;
	
	@Autowired 
	private RoleService roleService;
	
	@GetMapping("")
	public String listAll (ModelMap model) {
		List<User> listOfUsers = userService.listAll();
		model.put("listOfUsers", listOfUsers);
		
		return "users";
	}
	
	@GetMapping("/new")
	public String newUser(ModelMap model) {
		
		User user = new User();
		user.setEnabled(true);
		
		model.put("user", user);
		model.put("listOfRoles", roleService.retreiveListOfRoles());
		model.put("pageTitle", "Create New User");
		
		return "user_form";
	}
	
	
	@PostMapping("/save")
	public String postUserForm(User user, RedirectAttributes redirectAttributes) {
		userService.saveUser(user);
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully!");
		return "redirect:/users";
	}
	
	
	@PostMapping("/check_email")
	@ResponseBody
	public String checkDuplicateEmail(@Param("id") Integer id, @Param("email") String email) {
		
		return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
	}
	
	/*
	@PostMapping("/check_email")
	@ResponseBody
	public String checkDuplicateEmail(@RequestBody User user) {
		return userService.isEmailUnique(user) ? "OK" : "Duplicated";
	}*/
	
	@GetMapping("/edit/{id}" )
	public String editUser(@PathVariable(name = "id") Integer userId, ModelMap model, RedirectAttributes redirectAttributes) {
		try {
			model.put("user", userService.getById(userId));
			model.put("pageTitle", "Edit User (ID : " + userId + " )");
			model.put("listOfRoles", roleService.retreiveListOfRoles());

			return "user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}	
	}
	
	@GetMapping("/delete/{id}" )
	public String deleteUser(@PathVariable(name = "id") Integer userId, ModelMap model, RedirectAttributes redirectAttributes) {
		
		try {
			userService.delete(userId);
			
			redirectAttributes.addFlashAttribute("message" ,"The user ID " + userId + " has been deleted.");
			
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			
		}
		
		return "redirect:/users";
	}
		
	

}
