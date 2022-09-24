package com.ISMM.admin.categories;

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

import com.ISMM.admin.exceptions.CategoryNotFoundException;
import com.ISMM.common.domain.Category;

@Service
@Transactional
public class CategoryService {
	
	private static final int ROOT_INVENTORY_ITEM_PER_PAGE = 4;

	
	@Autowired
	CategoryRepository invRepo;
	
	public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum,
										  String sortDir, String keyword) {
		Sort sort = Sort.by("name");

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum - 1, ROOT_INVENTORY_ITEM_PER_PAGE, sort);

		Page<Category> pageCategories = null;
		
		if (keyword != null && !keyword.isEmpty()) {
			pageCategories = invRepo.search(keyword, pageable);	
		} else {
			pageCategories = invRepo.findRootCategories(pageable);
		}
		
		List<Category> rootCategories = pageCategories.getContent();

		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
				
		if (keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			for (Category item : searchResult) {
				item.setHasChildren(item.getChildren().size() > 0);
			}

			return searchResult;

		} else {
			return listHierarchicalRootInventory(rootCategories, sortDir);
		}
	}

	private List<Category> listHierarchicalRootInventory(List<Category> rootCategories, String sortDir) {
		List<Category> hierarchicalInventory = new ArrayList<>();
		
		for (Category rootInventory : rootCategories) {
			hierarchicalInventory.add(Category.copyFull(rootInventory));
			
			Set<Category> children = sortSubCategories(rootInventory.getChildren(), sortDir);
			
			for (Category subCategory : children) {
				String name = "--" + subCategory.getName();
				hierarchicalInventory.add(Category.copyFull(subCategory, name));
				
				listSubHierarchicalCategories(hierarchicalInventory, subCategory, 1, sortDir);
			}
		}
		
		return hierarchicalInventory;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories,
			Category parent, int subLevel, String sortDir) { 
		Set<Category> children = sortSubCategories(parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		
		for (Category subCategory : children) {
			String name = "";
			for (int i = 0; i < newSubLevel; i++) {				
				name += "--";
			}
			name += subCategory.getName();
		
			hierarchicalCategories.add(Category.copyFull(subCategory, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, subCategory, newSubLevel, sortDir); 
		}
		
	}

	public Category save(Category Category) {
		return invRepo.save(Category);
	}
	
	
	public List<Category> listCategoriesUsedInForm() {
		List<Category> CategoriesUsedInForm = new ArrayList<>();
		
		Iterable<Category> CategoriesInDB = invRepo.findRootCategories(Sort.by("name").ascending());
		
		
		for(Category invItem: CategoriesInDB ) {
			if(invItem.getParent() == null) {
				CategoriesUsedInForm.add(Category.copyIdAndName(invItem));
				
				Set<Category> children = sortSubCategories(invItem.getChildren());
				
				for (Category subCategories : children) {
					
					String name = "--" + subCategories.getName();
					CategoriesUsedInForm.add(Category.copyIdAndName(subCategories.getId(), name));
					
					listSubCategoriesUsedInForm(CategoriesUsedInForm,subCategories, 1);
				}
			}
		}
		
		return CategoriesUsedInForm;
	}
	
	private void listSubCategoriesUsedInForm(	List<Category> CategoryUsedInForm, 
												Category parent, int sublevel) {
		int newSubLevel = sublevel + 1;
		Set<Category> children = sortSubCategories(parent.getChildren());
		
		for (Category subInvItem : children) {
			String name = "";
			
			for (int i = 0; i < newSubLevel;i++) {
				name += "--";
			}
			name += subInvItem.getName();
			CategoryUsedInForm.add(Category.copyIdAndName(subInvItem.getId(), name));
			listSubCategoriesUsedInForm(CategoryUsedInForm, subInvItem, newSubLevel);
		}
	}
	
	public Category get(Integer id) throws CategoryNotFoundException {
		try {
			return invRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CategoryNotFoundException("Could not find any inventory item with ID " + id);
		}
	}
	
	
	public String checkUnique(Integer id, String name, String alias) {
		boolean isCreatingNew = (id == null || id == 0);

		Category CategoryByName = invRepo.findByName(name);

		if (isCreatingNew) {
			if (CategoryByName != null) {
				return "DuplicateName";
			} else {
				Category CategoryByAlias = invRepo.findByAlias(alias);
				if (CategoryByAlias != null) {
					return "DuplicateAlias";	
				}
			}
		} else {
			if (CategoryByName != null && CategoryByName.getId() != id) {
				return "DuplicateName";
			}
			Category CategoryByAlias = invRepo.findByAlias(alias);
			if (CategoryByAlias != null && CategoryByAlias.getId() != id) {
				return "DuplicateAlias";
			}
		}
		return "OK";
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children) {
		return sortSubCategories(children, "asc");
	}

	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {
			@Override
			public int compare(Category cat1, Category cat2) {
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
	
	public void updateCategoryEnabledStatus(Integer id, boolean enabled) {
		invRepo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws CategoryNotFoundException {
		Long countById = invRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any inventory item with ID " + id);
		}

		invRepo.deleteById(id);
	}
}