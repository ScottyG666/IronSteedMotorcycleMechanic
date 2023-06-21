package com.ISMM.admin.products;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ISMM.common.domain.Product;

@Service
@Transactional
public class ProductService {

	@Autowired private ProductRepository repo;

	
	/**
	 * Retrieves a complete list of products within the database in the form of an 
	 * 	Iterable of Products, that is type cast as a List of Products.
	 */
	public List<Product> listAll() {
		return (List<Product>) repo.findAll();
	}
	
	
	/**
	 * Saves the product into the database. If the Alias field is left
	 * 	blank during the creation of a new product, the Alias will assume
	 * 	the Product Name with the spaces replaced by dashes, as the 
	 * 	Alias will be used within the URL
	 * 
	 * @param product The Product object that is being edited within the HTML page
	 * @return Returns the Product object thats been saved for future flexibility
	 * 	within the controller
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
	 * @param id ID of the current Product Object being created/edited. Can be Null.
	 * @param name The name we are checking for within the database.
	 * @return "OK" when valid or "Duplicated" when invalid
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
	
	
	/**
	 * Reaches into the Product Repository, and changes the current status of the 
	 * 	selected product based on ID.
	 * 
	 * @param id ID of the Product to toggle
	 * @param enabled
	 */
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		repo.updateEnabledStatus(id, enabled);
	}	
	
	
	/**
	 * Deletes Product inside database based on the ID passed. If an invalid ID is passed,
	 * 	a custom message error message is created within a ProductNotFoundException.
	 * 
	 * @param id
	 * @throws ProductNotFoundException
	 */
	public void delete(Integer id) throws ProductNotFoundException {
		Long countById = repo.countById(id);

		if (countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);			
		}

		repo.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
		}
	}
	
}