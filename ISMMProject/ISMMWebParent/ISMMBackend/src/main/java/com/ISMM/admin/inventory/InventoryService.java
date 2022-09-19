package com.ISMM.admin.inventory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ISMM.admin.exceptions.InventoryItemNotFoundException;
import com.ISMM.admin.service.InventoryPageInfo;
import com.ISMM.common.domain.InventoryItem;

@Service
@Transactional
public class InventoryService {
	
	private static final int ROOT_INVENTORY_ITEM_PER_PAGE = 4;

	
	@Autowired
	InventoryRepository invRepo;
	
	public List<InventoryItem> listByPage(InventoryPageInfo pageInfo, int pageNum, String sortDir) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_INVENTORY_ITEM_PER_PAGE, sort);

		Page<InventoryItem> pageInventoryItems = invRepo.findRootInventoryItems(pageable);
		List<InventoryItem> rootInventoryItems = pageInventoryItems.getContent();

		pageInfo.setTotalElements(pageInventoryItems.getTotalElements());
		pageInfo.setTotalPages(pageInventoryItems.getTotalPages());
		return listHierarchicalInventory(rootInventoryItems, sortDir);
	}

	private List<InventoryItem> listHierarchicalInventory(List<InventoryItem> rootInventoryItems, String sortDir) { //
		List<InventoryItem> hierarchicalInventory = new ArrayList<>();
		
		for (InventoryItem rootInventory : rootInventoryItems) {
			hierarchicalInventory.add(InventoryItem.copyFull(rootInventory));
			
			Set<InventoryItem> children = sortSubInventoryItems(rootInventory.getChildren(), sortDir);
			
			for (InventoryItem subInventoryItem : children) {
				String name = "--" + subInventoryItem.getName();
				hierarchicalInventory.add(InventoryItem.copyFull(subInventoryItem, name));
				
				listSubHierarchicalInventoryItems(hierarchicalInventory, subInventoryItem, 1, sortDir);
			}
		}
		
		return hierarchicalInventory;
	}
	
	private void listSubHierarchicalInventoryItems(List<InventoryItem> hierarchicalInventoryItems,
			InventoryItem parent, int subLevel, String sortDir) { 
		Set<InventoryItem> children = sortSubInventoryItems(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		
		for (InventoryItem subInventoryItem : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "--";
			}
			name += subInventoryItem.getName();
		
			hierarchicalInventoryItems.add(InventoryItem.copyFull(subInventoryItem, name));
			
			listSubHierarchicalInventoryItems(hierarchicalInventoryItems, subInventoryItem, newSubLevel, sortDir); 
		}
		
	}

	public InventoryItem save(InventoryItem inventoryItem) {
		return invRepo.save(inventoryItem);
	}
	
	
	public List<InventoryItem> listInventoryItemsUsedInForm() {
		List<InventoryItem> inventoryItemsUsedInForm = new ArrayList<>();
		
		Iterable<InventoryItem> inventoryItemsInDB = invRepo.findRootInventoryItems(Sort.by("name").ascending());
		
		
		for(InventoryItem invItem: inventoryItemsInDB ) {
			if(invItem.getParent() == null) {
				inventoryItemsUsedInForm.add(InventoryItem.copyIdAndName(invItem));
				
				Set<InventoryItem> children = sortSubInventoryItems(invItem.getChildren());
				
				for (InventoryItem subInventoryitems : children) {
					
					String name = "--" + subInventoryitems.getName();
					inventoryItemsUsedInForm.add(InventoryItem.copyIdAndName(subInventoryitems.getId(), name));
					
					listSubInventoryItemsUsedInForm(inventoryItemsUsedInForm,subInventoryitems, 1);
				}
			}
		}
		
		return inventoryItemsUsedInForm;
	}
	
	private void listSubInventoryItemsUsedInForm(	List<InventoryItem> inventoryItemUsedInForm, 
												InventoryItem parent, int sublevel) {
		int newSubLevel = sublevel + 1;
		Set<InventoryItem> children = sortSubInventoryItems(parent.getChildren());
		
		for (InventoryItem subInvItem : children) {
			String name = "";
			
			for (int i = 0; i < newSubLevel;i++) {
				name += "--";
			}
			name += subInvItem.getName();
			inventoryItemUsedInForm.add(InventoryItem.copyIdAndName(subInvItem.getId(), name));
			listSubInventoryItemsUsedInForm(inventoryItemUsedInForm, subInvItem, newSubLevel);
		}
	}
	
	public InventoryItem get(Integer id) throws InventoryItemNotFoundException {
		try {
			return invRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new InventoryItemNotFoundException("Could not find any inventory item with ID " + id);
		}
	}
	
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		InventoryItem inventoryItemByName = invRepo.findByName(name);

		if (isCreatingNew) {
			if (inventoryItemByName != null) {
				return "DuplicateName";
			} else {
				InventoryItem inventoryItemByAlias = invRepo.findByAlias(alias);
				if (inventoryItemByAlias != null) {
					return "DuplicateAlias";	
				}
			}
		} else {
			if (inventoryItemByName != null && inventoryItemByName.getId() != id) {
				return "DuplicateName";
			}
			InventoryItem inventoryItemByAlias = invRepo.findByAlias(alias);
			if (inventoryItemByAlias != null && inventoryItemByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "OK";
	}
	
	private SortedSet<InventoryItem> sortSubInventoryItems(Set<InventoryItem> children) {
		return sortSubInventoryItems(children, "asc");
	}

	private SortedSet<InventoryItem> sortSubInventoryItems(Set<InventoryItem> children, String sortDir) {
		SortedSet<InventoryItem> sortedChildren = new TreeSet<>(new Comparator<InventoryItem>() {
			@Override
			public int compare(InventoryItem cat1, InventoryItem cat2) {
				if (sortDir.equals("asc")) {
					return cat1.getName().compareTo(cat2.getName());
				} else {
					return cat2.getName().compareTo(cat1.getName());
				}
			}
		});

		sortedChildren.addAll(children);

		return sortedChildren;
	}
	
	public void updateInventoryItemEnabledStatus(Integer id, boolean enabled) {
		invRepo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws InventoryItemNotFoundException {
		Long countById = invRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new InventoryItemNotFoundException("Could not find any inventory item with ID " + id);
		}

		invRepo.deleteById(id);
	}
}