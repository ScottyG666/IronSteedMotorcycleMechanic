package com.ISMM.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ISMM.admin.repository.UserRepository;
import com.ISMM.common.domain.User;

@Service
@Transactional
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
	
	
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = userRepo.getUserByEmail(email);
				
		if (userByEmail == null) return true;
		
		Boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null) return false;
		} else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}
	
	public void delete (Integer id) throws UserNotFoundException{
		
		Long countById = userRepo.countById(id);
		
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}
		
		userRepo.deleteById(id);
		
	}
	
	
	public void updateUserEnabledStatus (Integer id ,Boolean enabled) {
		
		userRepo.updateEnabledStatus(id, enabled);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
