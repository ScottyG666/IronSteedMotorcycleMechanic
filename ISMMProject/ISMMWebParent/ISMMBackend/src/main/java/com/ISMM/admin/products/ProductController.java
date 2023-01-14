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
	
	/**
	 * The Product listing page. Provides concise information about products, with buttons for quick access 
	 * 	to enable/disable a product, editing a product, and deleting a product.
	 */
	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Product> listProducts = prodService.listAll();

		model.put("listProducts", listProducts);

		return "products/products";
	}
	
	/**
	 * Controller mapping for creating a new Product object. The object is added to the model so that
	 * 	it's field's can be edited depending on the inputs.
	 */
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

	
	/**
	 * When the New / Edited Product passes the uniqueness validation, the form is submitted
	 * 	to this mapping where the product passed is added to the database. A confirmation 
	 * 	message is added to the model for user experience.
	 */
	@PostMapping("/save")
	public String saveProduct(Product product, RedirectAttributes ra) {
		prodService.save(product);
		ra.addFlashAttribute("message", "The product has been saved successfully.");

		return "redirect:/products";
	}
	
	
	/**
	 * Used when the Product form is submitted for New / Editing Products. 
	 */
	@PostMapping("/check_unique")
	@ResponseBody
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return prodService.checkUnique(id, name);
	}	

	
	/**
	 * When a request is sent to this mapping, checks for the designated Product actually exists. Then reverses 
	 * 	the enabled status of the Product found.
	 */
	@GetMapping("/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		prodService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The Product ID " + id + " has been " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}
	
	
	/**
	 * Deletes a product by a specified ID, which is passed along in the URL when the get request is sent. If
	 * 	the ID passed does not exist within the database, the user is alerted with a friendly custom message 
	 * 	from the Product Not Found Exception.
	 */
	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			ModelMap model, RedirectAttributes redirectAttributes) {
		try {
			prodService.delete(id);

			redirectAttributes.addFlashAttribute("message", 
					"The product ID " + id + " has been deleted successfully");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/products";
	}	
	
	
}