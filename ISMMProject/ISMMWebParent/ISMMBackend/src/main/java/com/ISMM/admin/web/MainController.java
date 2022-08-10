package com.ISMM.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	
	@GetMapping("")
	public String getAdminHome() {
		
		return "index";
	}
	
	@GetMapping("/login")
	public String getLoginPage () {
		return "login";
	}
}
