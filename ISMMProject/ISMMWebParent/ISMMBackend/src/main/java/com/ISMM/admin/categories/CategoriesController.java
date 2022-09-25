package com.ISMM.admin.categories;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ISMM.admin.exceptions.CategoryNotFoundException;
import com.ISMM.admin.service.FileUploadUtil;
import com.ISMM.common.domain.Category;

@Controller
@RequestMapping("/categories")
public class CategoriesController {

	@Autowired 
	CategoryService catService;

	@GetMapping("")
	public String listFirstPage(@Param("sortDir") String sortDir, ModelMap model) {
		return listByPage(1, sortDir, null, model);
	}
	
	@GetMapping("/page/{pageNum}") 
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, 
							 @Param("sortDir") String sortDir, String keyword,
							 ModelMap model) {
		if (sortDir ==  null || sortDir.isEmpty()) {
			sortDir = "asc";
		}
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = catService.listByPage(pageInfo, pageNum, sortDir, keyword);
		
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.put("totalPages", pageInfo.getTotalPages());
		model.put("totalItems", pageInfo.getTotalElements());
		model.put("currentPage", pageNum);
		model.put("sortField", "name");
		model.put("sortDir", sortDir);
		model.put("keyword", keyword);
		model.put("startCount", startCount);
		model.put("endCount", endCount);

		model.put("listCategories", listCategories);
		model.put("reverseSortDir", reverseSortDir);

		return "categories/categories";		
	}
	
	
	@GetMapping("/new") 
	public String newCategory (ModelMap model) {
		List<Category> listCategories = catService.listCategoriesUsedInForm();
		model.put("category", new Category());
		model.put("listCategories", listCategories);

		model.put("pageTitle", "Create New Category");
		
		return "categories/category_form";
	}
	
	@PostMapping("/save")
	public String saveCategory(	Category category, @RequestParam("fileImage") MultipartFile multipartFile,
								RedirectAttributes rA) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);

			Category savedCategory = catService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			catService.save(category);
		}	
		rA.addFlashAttribute("message", "The category has been saved successfully!");
		return "redirect:/categories";
	}
	
	@GetMapping("/edit/{id}")
	public String editCategory(@PathVariable(name = "id") Integer id, ModelMap model,
			RedirectAttributes rA) {
		try {
			Category category = catService.get(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();

			model.put("category", category);
			model.put("listCategories", listCategories);
			model.put("pageTitle", "Edit Category (ID: " + id + ")");

			return "categories/category_form";			
		} catch (CategoryNotFoundException ex) {
			rA.addFlashAttribute("message", ex.getMessage());
			return "redirect:/categories";
		}
	}
	
	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		catService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The category ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/categories";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, 
			ModelMap model,
			RedirectAttributes redirectAttributes) {
		try {
			catService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);

			redirectAttributes.addFlashAttribute("message", 
					"The category ID " + id + " has been deleted successfully");
		} catch (CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/categories";
	}	
	
	
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(	@Param("id") Integer id, @Param("name") String name,
								@Param("alias") String alias) {
		return catService.checkUnique(id, name, alias);
	}

}