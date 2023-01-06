package com.ISMM.admin.products;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

	public Product findByName(String name);
}