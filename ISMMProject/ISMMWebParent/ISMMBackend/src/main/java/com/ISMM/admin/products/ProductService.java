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
	
	/**
	 * When the ID passed into the function is null, then the 
	 * 	database is checked for any matching Product Name. Otherwise when 
	 * 	both ID and Product Name are populated within the function's 
	 * 	arguments, then the function will still verify if there is a 
	 * 	duplicate, but will let the duplication pass if the returned 
	 * 	product shares the same ID and NAME of the passed arguments.
	 * 
	 * @param id
	 * @param name
	 * @return
	 */
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repo.findByName(name);

		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}

		return "OK";
	}
	
}