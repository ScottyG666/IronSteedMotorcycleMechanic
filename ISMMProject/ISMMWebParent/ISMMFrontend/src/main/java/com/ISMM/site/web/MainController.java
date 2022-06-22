package com.ISMM.site.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	
	@GetMapping("")
	public String getUserWebsiteHome () {
		
		return "index";
	}
}
