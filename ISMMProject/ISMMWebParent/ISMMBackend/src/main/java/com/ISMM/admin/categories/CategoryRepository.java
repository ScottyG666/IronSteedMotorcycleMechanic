package com.ISMM.admin.categories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ISMM.common.domain.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Integer> {
	
	//To be deleted
	@Query("SELECT i FROM Category i WHERE i.parent.id is NULL")
	public List<Category> findRootCategories();

	@Query("SELECT i FROM Category i WHERE i.parent.id is NULL")
	public List<Category> findRootCategories(Sort sort);
	
	@Query("SELECT i FROM Category i WHERE i.parent.id is NULL")
	public Page<Category> findRootCategories(Pageable pageable);
	
	@Query("SELECT i FROM Category i WHERE i.name LIKE %?1%")
	public Page<Category> search(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	public Category findByName(String name);
	
	public Category findByAlias(String alias);
	
	@Query("UPDATE Category i SET i.enabled = ?2 WHERE i.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
}
