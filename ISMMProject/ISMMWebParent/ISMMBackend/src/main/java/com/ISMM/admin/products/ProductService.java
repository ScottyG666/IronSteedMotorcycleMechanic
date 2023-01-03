package com.ISMM.admin.products;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ISMM.common.domain.Product;

@Service
public class ProductService {

	@Autowired private ProductRepository repo;

	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	
	/**
	 * Saves the product into the database. If the Alias field is left
	 * 	blank during the creation of a new product, the Alias will assume
	 * 	the Product Name with the spaces replaced by dashes, as the 
	 * 	Alias will be used within the URL
	 * 
	 * @param product
	 * @return product
	 */
	public Product save(Product product) {
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}

		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}

		product.setUpdatedTime(new Date());

		return repo.save(product);
	}
}