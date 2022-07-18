package com.ISMM.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ISMM.admin.repository.UserRepository;
import com.ISMM.common.domain.User;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public List<User> listAll() {
		List<User> listOfUsers = new ArrayList<>();
		
		userRepo.findAll().forEach(listOfUsers::add);
		
		return listOfUsers;
	}



	public void saveUser(User user) {
		encodePassword(user);
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	 
	
	public User findByEmail ( String email) {
		
		return userRepo.getUserByEmail(email);
	}



	public boolean isEmailUnique(String email) {
		User userByEmail = userRepo.getUserByEmail(email);
		return userByEmail == null;
	}



	public User getById(Integer userId) throws UserNotFoundException {
		try {
			return userRepo.findById(userId).get();			
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}
		
	}
	
}
