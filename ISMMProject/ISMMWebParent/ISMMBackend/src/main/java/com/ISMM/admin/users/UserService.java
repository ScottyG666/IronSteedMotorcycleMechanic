package com.ISMM.admin.users;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ISMM.admin.exceptions.UserNotFoundException;
import com.ISMM.common.domain.User;

@Service
@Transactional
public class UserService {
	
	public static final int USERS_PER_PAGE = 5;

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public List<User> listAll() {
		List<User> listOfUsers = new ArrayList<>();
		
		userRepo.findAll(Sort.by("firstName").ascending()).forEach(listOfUsers::add);
		
		return listOfUsers;
	}
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyWord) {
		
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, USERS_PER_PAGE, sort);
		
		if(keyWord != null ) {
			return userRepo.findAll(keyWord, pageable);
		}
		return userRepo.findAll(pageable);
	}

	public User saveUser(User user) {
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
		return userRepo.save(user);
	}
	
	
	/**
	 * 	Limits the fields the user is able to edit within their own profile.
	 * 	Fields that persist if left empty: Password & Photos
	 * 	Fields that get saved every time: First name and Last name
	 * @param userInForm
	 * @return A User Object for re-displaying to the console
	 */
	public User updateAccount(User userInForm) {
		User userInDB =  userRepo.findById(userInForm.getId()).get();
		
		if(!userInForm.getPassword().isEmpty()) {
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		
		if(userInForm.getPhotos() != null) {
			userInDB.setPhotos(userInForm.getPhotos());
		}
		
		userInDB.setFirstName(userInForm.getFirstName());
		userInDB.setLastName(userInForm.getLastName());
		
		return userRepo.save(userInDB);
	
	}
	
	private void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	 
	
	public User getByEmail ( String email) {
		
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
	
	
	public String getRedirectURLOfAffectedUser (User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyWord=" + firstPartOfEmail;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
