package com.ISMM.admin.web;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ISMM.admin.service.CategoryService;
import com.ISMM.admin.service.FileUploadUtil;
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
	
	@PostMapping("/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
		
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		
		Category savedCategory = catService.save(category);
				
		String uploadDir = "../category-images/" + savedCategory.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
		return "redirect:/categories";
	}

}
