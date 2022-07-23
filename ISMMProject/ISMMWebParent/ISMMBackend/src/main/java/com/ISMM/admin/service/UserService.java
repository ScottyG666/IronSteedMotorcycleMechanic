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
		Boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		userRepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	 
	
	public User findByEmail ( String email) {
		
		return userRepo.getUserByEmail(email);
	}

	public User getById(Integer userId) throws UserNotFoundException {
		try {
			return userRepo.findById(userId).get();			
		} catch (NoSuchElementException e) {
			throw new UserNotFoundException("Could not find any user with ID " + userId);
		}
		
	}
	
	/*
	 * 	This method is used to check if the email is unique when creating or editing a User
	 * 	++If the email is the email of an existing user :
	 * 		--this will return true if the email matches the retrieved user OR the Email does NOT
	 * 			belong in the DataBase already
	 * 		--This will return false if it matches an email from a user in the database that is 
	 * 			NOT the retrieved user
	 * 
	 */
	public boolean isEmailUnique(User user) {
		User userByEmail = userRepo.getUserByEmail(user.getEmail());
		
		if (userByEmail != null) {
			if (userByEmail.getId().equals(user.getId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;			
		}
		
		
	}

	
	public void delete (Integer id) throws UserNotFoundException{
		
		Long countById = userRepo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
		userRepo.deleteById(id);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
