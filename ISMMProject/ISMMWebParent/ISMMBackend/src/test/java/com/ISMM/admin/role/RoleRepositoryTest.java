package com.ISMM.admin.role;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.ISMM.admin.repository.RoleRepository;
import com.ISMM.common.domain.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	
	@Autowired
	private RoleRepository repo;
	/*
	 * Test for initializing the roles table into the database and adding the 
	 * first role(Admin) into the mysql table, assigning it an auto incremented
	 * ID, Name, and a Description
	 */
	@Test
	public void testCreateFirstRole() {
		Role adminRole = new Role("Admin", "Manage everything.");
		
		Role savedRole = repo.save(adminRole);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	/*
	 * Initializes the other roles within the Motorcycle mechanic shop including:
	 * 	++ Shop Manager
	 * 	++ Mechanic
	 * 	++ Front Desk
	 * 	++ Warehouse
	 */
	
	@Test
	public void testCreateAdditionalRoles() {
<<<<<<< HEAD
		
=======
>>>>>>> main
		Role roleShopManager = new Role("Shop Manager", "Oversees hiring, training, and evaluating"
				+ " mechanics and support staff.");
		
		Role roleMechanic = new Role("Mechanic", "Performes maintenence and service "
				+ "activities.");
		
		Role roleFrontDesk = new Role("Front Desk", "Manage appointments, payments, walk-ins, "
				+ "and other clerical duties.");
		
		Role roleMechanicSupportStaff = new Role("Mechanic Support Staff", "Assist mechanics with "
				+ "repetative tasks.");
		
		
		repo.saveAll(List.of(roleShopManager, roleMechanic, roleFrontDesk, roleMechanicSupportStaff));
	}
}
