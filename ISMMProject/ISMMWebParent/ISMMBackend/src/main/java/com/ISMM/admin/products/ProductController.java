package com.ISMM.admin.products;

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

import com.ISMM.admin.brands.BrandService;
import com.ISMM.common.domain.Brand;
import com.ISMM.common.domain.Product;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	
	@Autowired
	private ProductService prodService;
	@Autowired
	private BrandService brandService;
	
	
	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Product> listProducts = prodService.listAll();

		model.put("listProducts", listProducts);

		return "products/products";
	}
	
	
	@GetMapping("/new")
	public String newProduct(ModelMap model) {
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);

		model.put("product", product);
		model.put("listBrands", listBrands);
		model.put("pageTitle", "Create New Product");

		return "products/product_form";
	}

	@PostMapping("/save")
	public String saveProduct(Product product, RedirectAttributes ra) {
		prodService.save(product);
		ra.addFlashAttribute("message", "The product has been saved successfully.");

		return "redirect:/products";
	}
	
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return prodService.checkUnique(id, name);
	}	

	
	
	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		prodService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}
	
}