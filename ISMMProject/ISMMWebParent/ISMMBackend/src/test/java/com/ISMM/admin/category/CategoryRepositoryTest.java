package com.ISMM.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.repository.CategoryRepository;
import com.ISMM.common.domain.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTest {

	@Autowired
	private CategoryRepository catRepo;
	
	@Test
	public void testCreateRootCategory() {
		Category category = new Category("Exhaust");
		Category savedCategory = catRepo.save(category);
		
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}
	
	//LEFT OFF @ 11:30 VIDEO 68
	
	@Test
	public void createSubCategory() {
		Category parent = new Category(1);
		//Category subCategory = new Category("Desktop", parent);
		Category laptops = new Category("Laptops", parent);
		Category computerComponents= new Category("Computer Components", parent);
		
		catRepo.saveAll(List.of(laptops, computerComponents));
		
		//assertThat(savedCategory.getId()).isGreaterThan(0);
	}
}
