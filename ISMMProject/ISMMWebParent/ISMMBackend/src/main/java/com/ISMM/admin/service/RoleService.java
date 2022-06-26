package com.ISMM.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ISMM.admin.repository.RoleRepository;
import com.ISMM.common.domain.Role;

@Service
@Component
public class RoleService {

	@Autowired
	private RoleRepository roleRepo;
	
	public Role CreateRole(String name, String description) {
		Role savedRole = roleRepo.save(new Role(name, description));
		return savedRole;
	}
	
	
	
	public List<Role> listRoles() {
	
		
		List<Role> listOfRoles = StreamSupport.stream(roleRepo.findAll()::spliterator, false)
											  .Collect(Collectors.toList());
		
		return null;
	}
	
}
