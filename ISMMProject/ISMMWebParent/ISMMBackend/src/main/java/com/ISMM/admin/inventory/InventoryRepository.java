package com.ISMM.admin.inventory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ISMM.common.domain.InventoryItem;

public interface InventoryRepository extends PagingAndSortingRepository<InventoryItem, Integer> {
	
	//To be deleted
	@Query("SELECT i FROM InventoryItem i WHERE i.parent.id is NULL")
	public List<InventoryItem> findRootInventoryItems();

	@Query("SELECT i FROM InventoryItem i WHERE i.parent.id is NULL")
	public List<InventoryItem> findRootInventoryItems(Sort sort);
	
	@Query("SELECT i FROM InventoryItem i WHERE i.parent.id is NULL")
	public Page<InventoryItem> findRootInventoryItems(Pageable pageable);
	
	@Query("SELECT i FROM InventoryItem i WHERE i.name LIKE %?1%")
	public Page<InventoryItem> search(String keyword, Pageable pageable);
	
	public Long countById(Integer id);
	
	public InventoryItem findByName(String name);
	
	public InventoryItem findByAlias(String alias);
	
	@Query("UPDATE InventoryItem i SET i.enabled = ?2 WHERE i.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
}
