package com.ISMM.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ISMM.common.domain.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {

}
