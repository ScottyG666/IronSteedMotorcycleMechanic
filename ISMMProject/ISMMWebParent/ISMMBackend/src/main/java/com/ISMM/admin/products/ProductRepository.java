package com.ISMM.admin.products;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	public Product findByName(String name);
	
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
	

	public Long countById(Integer id);
}