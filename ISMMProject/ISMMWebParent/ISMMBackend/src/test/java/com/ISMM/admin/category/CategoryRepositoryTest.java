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

import com.ISMM.admin.repository.CategoryRepository;
import com.ISMM.common.domain.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository catRepo;
	
	@Test
	public void testCreateRootCategory() {
		
		 	Category category = new Category("Computer");
			Category savedCategory = catRepo.save(category);
			
			assertThat(savedCategory.getId()).isGreaterThan(0);
			
	
			Category secondCategory = new Category("Electronics");
			Category secondarySavedCategory = catRepo.save(secondCategory);
			
			assertThat(secondarySavedCategory.getId()).isGreaterThan(0);
		
	}
	
	
	@Test
	public void createSubCategorys() {
		Category primaryParent = new Category(1);
		Category primarySubCategory = new Category("Desktop", primaryParent);
		Category primarySavedCategory = catRepo.save(primarySubCategory);
		assertThat(primarySavedCategory.getId()).isGreaterThan(0);
		
		
		Category laptops = new Category("Laptops", primaryParent);
		Category components = new Category("Computer Components", primaryParent);
		catRepo.saveAll(List.of(laptops, components));
		 
		
		Category secondaryParent = new Category(2);

		Category smartphones = new Category("Smartphones", secondaryParent);
		Category cameras = new Category("Cameras", secondaryParent);
		catRepo.saveAll(List.of(cameras, smartphones));
	}
	
	
	
	@Test 
	public void creatingChildSubcategory () {
		Category parentSubCategory = new Category(5);
		Category childSubCategory = new Category("Memory", parentSubCategory);
		
		Category savedSubCategoryChild = catRepo.save(childSubCategory);
		assertThat(savedSubCategoryChild.getId()).isGreaterThan(0);
	}
	
	@Test
	public void createAdditionalChildSubCategorys() {
		
		Category smartphoneParent = new Category(7);
		Category smartphoneSubCategory = new Category("IPhone", smartphoneParent);
		Category savedSmartphoneSubCategory = catRepo.save(smartphoneSubCategory);
		assertThat(savedSmartphoneSubCategory.getId()).isGreaterThan(0);
		
		Category laptopParent = new Category(4);
		Category laptopSubCategory = new Category("Gaming Laptops", laptopParent);
		Category savedLaptopSubCategory = catRepo.save(laptopSubCategory);
		assertThat(savedLaptopSubCategory.getId()).isGreaterThan(0);
		
		
	}

	
	@Test
	public void testGetCategory () {
		Category category = catRepo.findById(2).get();
		System.out.println(category.getName());
		
		Set<Category> children = category.getChildren();
		for (Category subCategory : children) {
			System.out.println(subCategory.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}
	
	@Test
	public void testPrintHierarchicalCategories () {
		Iterable<Category> categories = catRepo.findAll();
		
		for(Category category: categories ) {
			if(category.getParent() == null) {
				System.out.println(category.getName());
				
				Set<Category> children = category.getChildren();
				
				for (Category subCategory : children) {
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
		}
	}
	
	private void printChildren(Category parent, int sublevel) {
		int newSubLevel = sublevel + 1;
		
		Set<Category> children = parent.getChildren();
		
		for (Category subCategory : children) {
			for (int i = 0; i < newSubLevel;i++) {
				System.out.print("--");
			}
			System.out.println(subCategory.getName());
		}
	}
	
	
	@Test 
	public void testListRootCategories() {
		List<Category> rootCategories = catRepo.listRootCategories();
		
		rootCategories.forEach(cat -> System.out.println(cat.getName()));
	}
	
}
