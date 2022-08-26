package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.ISMM.admin.service.CategoryService;
import com.ISMM.common.domain.Category;

@Controller
public class CategoriesController {

	@Autowired 
	CategoryService catService;

	@GetMapping("/categories")
	public String listAll(ModelMap model) {
		List<Category> listCategories = catService.listAll();
		
		model.put("listCategories", listCategories);
		return "categories/categories";  
	}

}
