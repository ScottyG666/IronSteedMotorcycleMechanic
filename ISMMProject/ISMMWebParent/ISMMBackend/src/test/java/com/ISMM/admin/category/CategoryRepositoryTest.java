package com.ISMM.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.repository.InventoryRepository;
import com.ISMM.common.domain.InventoryItem;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private InventoryRepository catRepo;
	
	@Test
	public void testCreateRootCategory() {
		
		 	InventoryItem category = new InventoryItem("Computer");
			InventoryItem savedCategory = catRepo.save(category);
			
			assertThat(savedCategory.getId()).isGreaterThan(0);
			
	
			InventoryItem secondCategory = new InventoryItem("Electronics");
			InventoryItem secondarySavedCategory = catRepo.save(secondCategory);
			
			assertThat(secondarySavedCategory.getId()).isGreaterThan(0);
		
	}
	
	
	@Test
	public void createSubCategorys() {
		InventoryItem primaryParent = new InventoryItem(1);
		InventoryItem primarySubCategory = new InventoryItem("Desktop", primaryParent);
		InventoryItem primarySavedCategory = catRepo.save(primarySubCategory);
		assertThat(primarySavedCategory.getId()).isGreaterThan(0);
		
		
		InventoryItem laptops = new InventoryItem("Laptops", primaryParent);
		InventoryItem components = new InventoryItem("Computer Components", primaryParent);
		catRepo.saveAll(List.of(laptops, components));
		 
		
		InventoryItem secondaryParent = new InventoryItem(2);

		InventoryItem smartphones = new InventoryItem("Smartphones", secondaryParent);
		InventoryItem cameras = new InventoryItem("Cameras", secondaryParent);
		catRepo.saveAll(List.of(cameras, smartphones));
	}
	
	
	
	@Test 
	public void creatingChildSubcategory () {
		InventoryItem parentSubCategory = new InventoryItem(5);
		InventoryItem childSubCategory = new InventoryItem("Memory", parentSubCategory);
		
		InventoryItem savedSubCategoryChild = catRepo.save(childSubCategory);
		assertThat(savedSubCategoryChild.getId()).isGreaterThan(0);
	}
	
	@Test
	public void createAdditionalChildSubCategorys() {
		
		InventoryItem smartphoneParent = new InventoryItem(7);
		InventoryItem smartphoneSubCategory = new InventoryItem("IPhone", smartphoneParent);
		InventoryItem savedSmartphoneSubCategory = catRepo.save(smartphoneSubCategory);
		assertThat(savedSmartphoneSubCategory.getId()).isGreaterThan(0);
		
		InventoryItem laptopParent = new InventoryItem(4);
		InventoryItem laptopSubCategory = new InventoryItem("Gaming Laptops", laptopParent);
		InventoryItem savedLaptopSubCategory = catRepo.save(laptopSubCategory);
		assertThat(savedLaptopSubCategory.getId()).isGreaterThan(0);
		
		
	}

	
	@Test
	public void testGetCategory () {
		InventoryItem category = catRepo.findById(2).get();
		System.out.println(category.getName());
		
		Set<InventoryItem> children = category.getChildren();
		for (InventoryItem subCategory : children) {
			System.out.println(subCategory.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories () {
		Iterable<InventoryItem> categories = catRepo.findAll();
		
		for(InventoryItem category: categories ) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<InventoryItem> children = category.getChildren();
				
				for (InventoryItem subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(InventoryItem parent, int sublevel) {
		int newSubLevel = sublevel + 1;
		
		Set<InventoryItem> children = parent.getChildren();
		
		for (InventoryItem subCategory : children) {
			for (int i = 0; i < newSubLevel;i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
		}
	}
	
	
	@Test 
	public void testListRootCategories() {
		List<InventoryItem> rootCategories = catRepo.findRootInventoryItems();
		
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
	
	@Test
	public void testFindByName() {
		String name = "Computer";
		InventoryItem foundCategory = catRepo.findByName(name);
		
		assertThat(foundCategory).isNotNull();
		assertThat(foundCategory.getName()).isEqualTo(name);
		
	}
	
	@Test
	public void testFindByAlias() {
		String alias = "Electronics";
		InventoryItem foundCategory = catRepo.findByName(alias);
		
		assertThat(foundCategory).isNotNull();
		assertThat(foundCategory.getName()).isEqualTo(alias);
		
	}
}
