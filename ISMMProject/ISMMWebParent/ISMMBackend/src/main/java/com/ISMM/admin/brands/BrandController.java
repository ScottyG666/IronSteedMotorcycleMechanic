package com.ISMM.admin.brands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.ISMM.admin.categories.CategoryService;
import com.ISMM.admin.service.FileUploadUtil;
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
	public String listFirstPage(ModelMap model) {
		return listByPage(1, model, "name" , "asc", null);
	}
	
	@GetMapping("/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum,
				ModelMap model, @Param("sortField") String sortField,
				@Param("sortDir") String sortDir, @Param("keyword") String keyword ) {
		Page<Brand> page = brandService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Brand> listBrands = page.getContent();
		
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1;
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1;
		if (endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.put("currentPage", pageNum);
		model.put("totalPages", page.getTotalPages());
		model.put("startCount", startCount);
		model.put("endCount", endCount);
		model.put("totalItems", page.getTotalElements());
		model.put("sortField", sortField);
		model.put("sortDir", sortDir);
		model.put("reverseSortDir", reverseSortDir);
		model.put("keyword", keyword);		
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
	
	@PostMapping("/save")
	public String saveBrand(Brand brand, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes ra) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);

			Brand savedBrand = brandService.save(brand);
			String uploadDir = "../brand-logos/" + savedBrand.getId();

			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

		} else {
			brandService.save(brand);
		}

		ra.addFlashAttribute("message", "The brand has been saved successfully.");
		return "redirect:/brands";		
	}

	@GetMapping("/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, ModelMap model,
							RedirectAttributes ra) {
		try {
			Brand brand = brandService.get(id);
			List<Category> listCategories = catService.listCategoriesUsedInForm();

			model.put("brand", brand);
			model.put("listCategories", listCategories);
			model.put("pageTitle", "Edit Brand (ID: " + id + ")");

			return "brands/brand_form";			
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}

	@GetMapping("/delete/{id}")
	public String deleteBrand(	@PathVariable(name = "id") Integer id, 
								ModelMap model,
								RedirectAttributes redirectAttributes) {
		try {
			brandService.delete(id);
			String brandDir = "../brand-logos/" + id;
			FileUploadUtil.removeDir(brandDir);

			redirectAttributes.addFlashAttribute("message", 
					"The brand ID " + id + " has been deleted successfully");
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/brands";
	}		
	
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(	@Param("id") Integer id, @Param("name") String name) {
		return brandService.checkUnique(id, name);
	}
	
	
	@GetMapping("/{id}/categories")
	@ResponseBody
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundRestException {
		List<CategoryDTO> listCategories = new ArrayList<>(); 
		
		
		try {
			Brand brand = brandService.get(brandId);
			Set<Category> categories = brand.getCategories();

			for (Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}
			return listCategories;
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
		}
	}
	
	
}