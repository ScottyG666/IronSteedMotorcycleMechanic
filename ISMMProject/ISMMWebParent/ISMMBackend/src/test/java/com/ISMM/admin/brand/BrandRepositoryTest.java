package com.ISMM.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.brands.BrandRepository;
import com.ISMM.common.domain.Brand;
import com.ISMM.common.domain.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {

	
	@Autowired
	private BrandRepository brandRepo;
	
	
	@Test
	public void testCreateBrand1() {
		Category helmets = new Category(12);
		Brand ruroc = new Brand("Ruroc");
		ruroc.getCategories().add(helmets);

		Brand savedBrand = brandRepo.save(ruroc);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCreateBrand2() {
		Category goggles = new Category(10);
		Category gloves = new Category(11);

		Brand indian = new Brand("Indian");
		indian.getCategories().add(goggles);
		indian.getCategories().add(gloves);

		Brand savedBrand = brandRepo.save(indian);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
		
	}
	
	@Test
	public void testCreateBrand3() {
		Brand dianese = new Brand("Dianese");

		dianese.getCategories().add(new Category(13));	// category jackets
		dianese.getCategories().add(new Category(8));	// category saddle bags

		Brand savedBrand = brandRepo.save(dianese);

		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll() {
		Iterable<Brand> brands = brandRepo.findAll();
		brands.forEach(System.out::println);
		
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById() {
		Brand brand = brandRepo.findById(1).get();
		
		assertThat(brand.getName()).isEqualTo("Ruroc");
	}
	
	@Test
	public void testUpdateName() {
		String newName = "Speed and Strength";
		Brand speedAndStrength = brandRepo.findById(3).get();
		speedAndStrength.setName(newName);
		
		Brand savedBrand = brandRepo.save(speedAndStrength);
		assertThat(savedBrand.getName()).isEqualTo(newName);
	}
	
	@Test
	public void testDelete() {
		Integer id = 2;
		brandRepo.deleteById(id);
		
		Optional<Brand> result = brandRepo.findById(id);
		
		assertThat(result.isEmpty());
	}
	
}
