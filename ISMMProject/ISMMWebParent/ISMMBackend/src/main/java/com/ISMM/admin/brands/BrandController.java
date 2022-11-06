package com.ISMM.admin.brands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ISMM.admin.categories.CategoryService;
import com.ISMM.common.domain.Brand;
import com.ISMM.common.domain.Category;

@Controller
@RequestMapping("/brands")
public class BrandController {

	@Autowired
	private BrandService brandService;
	
	@Autowired
	private CategoryService catService;

	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Brand> listBrands = brandService.listAll();
		model.put("listBrands", listBrands);

		return "brands/brands";
	}
	
	@GetMapping("/new")
	public String newBrand(ModelMap model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();

		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Create New Brand");

		return "brands/brand_form";		
	}
}