package com.ISMM.admin.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ISMM.common.domain.Product;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	
	@Autowired private ProductService prodService;

	@GetMapping("")
	public String listAll(ModelMap model) {
		List<Product> listProducts = prodService.listAll();

		model.put("listProducts", listProducts);

		return "products/products";
	}
}