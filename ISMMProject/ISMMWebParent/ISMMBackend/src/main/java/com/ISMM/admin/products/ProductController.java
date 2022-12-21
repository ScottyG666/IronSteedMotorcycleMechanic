package com.ISMM.admin.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String saveProduct(Product product) {
		System.out.println("Product Name: " + product.getName());
		System.out.println("Brand ID: " + product.getBrand().getId());
		System.out.println("Category ID: " + product.getCategory().getId());

		return "redirect:/products";
	}


	
}