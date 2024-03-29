package com.ISMM.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.users.UserRepository;
import com.ISMM.common.domain.Role;
import com.ISMM.common.domain.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	/*		RUN *ROLEREPOSITORYTEST* BEFORE RUNNING THESE TESTS IN ORDER TO POPULATE
	 * 			THE ROLES THAT THE USERS ARE CREATED WITH, OTHERWISE EACH USER WILL HAVE
	 * 			NO ROLES ASSIGNED
	 */
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole()
	{
		Role adminRole = entityManager.find(Role.class, 1);
		
		User testUser = new User("test.user@email.com", "testPassword" , "testFirstN", "testLastN");
		testUser.addRole(adminRole);
		
		User savedUser = userRepo.save(testUser);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	
	@Test
	public void testCreateNewUserWithTwoRoles() {
		Role mechanicRole = entityManager.find(Role.class, 2);
		Role warehouseRole = entityManager.find(Role.class, 4);
		
		User testUser = new User("Second.Test@email.com", "Password2" , "Samuel", "Jackson");
		testUser.addRole(mechanicRole);
		testUser.addRole(warehouseRole);
		
		
		User savedUser = userRepo.save(testUser);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> usersList =  userRepo.findAll();
		
		usersList.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User retrievedUser = userRepo.findById(1).get();
		
		assertThat(retrievedUser).isNotNull();
		
	}
	
	
	@Test
	public void testUpdateUserDetails() {
		User testingUser = userRepo.findById(1).get();

		testingUser.setEnabled(true);
		testingUser.setEmail("UpdatedEmail@email.com");
		userRepo.save(testingUser);
		
		
		System.out.println(testingUser);
	}
	
	
	@Test
	public void testUpdatedUserRoles () {
		User testSecondUser = userRepo.findById(2).get();
		
		Role roleFrontDesk = new Role(3);
		Role roleMechanic = new Role(2);
		
		testSecondUser.getUserRoles().remove(roleMechanic);
		testSecondUser.getUserRoles().add(roleFrontDesk);
		
		userRepo.save(testSecondUser);
		
		System.out.println(testSecondUser);
		
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		userRepo.deleteById(userId);
		
	}
	
	@Test
	public void testGetUserByEmail () {
		String email = "test.user@email.com";
		User user = userRepo.getUserByEmail(email);
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById () {
		Integer id = 1;
		
		Long countById = userRepo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
		
	}
	
	@Test 
	public void testDisableUser() {
		Integer id = 26;
		userRepo.updateEnabledStatus(id, false);
	}
	
	
	@Test 
	public void testEnableUser() {
		Integer id = 26;
		userRepo.updateEnabledStatus(id, true);
	}
	
	
	/*
	 * This test is ran with 23 users imported from sample data
	 */
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(pageable);
		
		List<User> listOfUsers = page.getContent();
		listOfUsers.forEach(user -> System.out.println(user));
		
		assertThat(listOfUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = userRepo.findAll(keyword, pageable);
		
		List<User> listOfUsers = page.getContent();
		
		listOfUsers.forEach(user -> System.out.println(user));
		
		assertThat(listOfUsers.size()).isGreaterThan(0);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
