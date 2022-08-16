package com.ISMM.admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ISMM.admin.security.ISMMUserDetails;
import com.ISMM.admin.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Autowired 
	private UserService userService;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ISMMUserDetails loggedUser, 
							  ModelMap model) {
		String email = loggedUser.getUsername();
		model.put("user", userService.getByEmail(email));
		
		
		return "account_form";
	}
}
