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
	public void testCreate2Product() {
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
	public void testCreate3Product() {
		Brand brand = entityManager.find(Brand.class, 1);
		Category category = entityManager.find(Category.class, 5);

		Product product = new Product();
		product.setName("Atlas 4.0 Helmet- El Cobre");
		product.setAlias("atlas_el_cobre");
		product.setShortDescription("ATLAS 4.0 is one of the first motorcycle helmets on the road to meet ECE 22.06. ");
		product.setFullDescription("Full description for El Cobra Helmet");

		product.setBrand(brand);
		product.setCategory(category);

		product.setPrice(525);
		product.setCost(250);
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
}