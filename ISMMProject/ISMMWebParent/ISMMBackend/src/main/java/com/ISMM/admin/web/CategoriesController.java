package com.ISMM.admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ISMM.admin.service.CategoryService;
import com.ISMM.common.domain.Category;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired 
	CategoryService catService;

	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Category> listCategories = catService.listAll();
		
		model.put("listCategories", listCategories);
		return "categories/categories";  
	}
	
	@GetMapping("/new") 
	public String newCategory (ModelMap model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();
		model.put("category", new Category());
		model.put("listCategories", listCategories);

		model.put("pageTitle", "Create New Category");
		
		return "categories/categories_form";
	}

}
