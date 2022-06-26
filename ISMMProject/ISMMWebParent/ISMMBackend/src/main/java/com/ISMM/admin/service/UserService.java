package com.ISMM.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ISMM.admin.repository.UserRepository;
import com.ISMM.common.domain.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	
	
	public List<User> listAll() {
		List<User> listOfUsers = new ArrayList<>();
		
		userRepo.findAll().forEach(listOfUsers::add);
		
		return listOfUsers;
	}
	
	
}
