package com.ISMM.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.products.ProductRepository;
import com.ISMM.common.domain.Brand;
import com.ISMM.common.domain.Category;
import com.ISMM.common.domain.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository prodRepo;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void testCreateProduct() {
		Brand brand = entityManager.find(Brand.class, 4);
		Category category = entityManager.find(Category.class, 20);

		Product product = new Product();
		product.setName("Pirelli Tires Diablo SuperCorse SP V2");
		product.setAlias("diablo_supercorse_sp_v2");
		product.setShortDescription("Short description for Diablo SuperCorse SP V2");
		product.setFullDescription("Full description for Diablo SuperCorse SP V2");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(420);
		product.setCost(100);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = prodRepo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateSecondProduct() {
		Brand brand = entityManager.find(Brand.class, 5);
		Category category = entityManager.find(Category.class, 31);

		Product product = new Product();
		product.setName("Olympia 174-Cool Men's Gloves");
		product.setAlias("olympia_174_cool_gloves");
		product.setShortDescription("714 Cool Men's Gloves by Olympia Gloves®. This top-grade product is"
									+ " expertly made in compliance with stringent industry standards to "
									+ "offer a fusion of a well-balanced design and high level of craftsmanship.");
		product.setFullDescription("Full description for 714 Cool Men's Gloves by Olympia Gloves");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(35);
		product.setCost(12);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = prodRepo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateThirdProduct() {
		Brand brand = entityManager.find(Brand.class, 17);
		Category category = entityManager.find(Category.class, 41);

		Product product = new Product();
		product.setName("Mustanf Wide Tripper Forward Passanger Seat");
		product.setAlias("mustang_wide_tripper");
		product.setShortDescription("Wide Tripper Forward Passenger Seat by Mustang. Cut low with a wide 13\" driver seat that has extra width in the hip area for all-day comfort.");
		product.setFullDescription("Full description for Wide Tripper Forward Passenger Seat by Mustang");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(225);
		product.setCost(95);
		product.setEnabled(true);
		product.setInStock(true);

		product.setCreatedTime(new Date());
		product.setUpdatedTime(new Date());

		Product savedProduct = prodRepo.save(product);

		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = prodRepo.findAll();

		iterableProducts.forEach(System.out::println);
	}

	@Test
	public void testGetProduct() {
		Integer id = 2;
		Product product = prodRepo.findById(id).get();
		System.out.println(product);

		assertThat(product).isNotNull();
	}

	@Test
	public void testUpdateProduct() {
		Integer id = 1;
		Product product = prodRepo.findById(id).get();
		product.setPrice(499);

		prodRepo.save(product);

		Product updatedProduct = entityManager.find(Product.class, id);

		assertThat(updatedProduct.getPrice()).isEqualTo(499);
	}

	@Test
	public void testDeleteProduct() {
		Integer id = 2;
		prodRepo.deleteById(id);

		Optional<Product> result = prodRepo.findById(id);

		assertThat(!result.isPresent());
	}
	
	
	@Test
	public void testSaveProductWithImages() {
		Integer productId = 1;
		Product product = prodRepo.findById(productId).get();

		product.setMainImage("main image.jpg");
		product.addExtraImage("extra image 1.png");
		product.addExtraImage("extra_image_2.png");
		product.addExtraImage("extra-image3.png");

		Product savedProduct = prodRepo.save(product);

		assertThat(savedProduct.getImages().size()).isEqualTo(3);
	}
	
	
	@Test
	public void testSaveProductWithDetails() {
		Integer productId = 1;
		Product product = prodRepo.findById(productId).get();
		
		product.addDetail("Device Memory", "128 GB");
		product.addDetail("CPU Model", "MediaTek");
		product.addDetail("OS", "Android 10");
		
		Product savedProduct = prodRepo.save(product);
		assertThat(savedProduct.getDetails()).isNotEmpty();
	}
	
}
