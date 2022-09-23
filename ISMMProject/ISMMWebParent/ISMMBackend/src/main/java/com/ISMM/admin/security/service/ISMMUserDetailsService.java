package com.ISMM.admin.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ISMM.admin.security.ISMMUserDetails;
import com.ISMM.admin.users.UserRepository;
import com.ISMM.common.domain.User;

@Service
public class ISMMUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		
		if (user != null) {
			return new ISMMUserDetails(user);
		}

		throw new UsernameNotFoundException("Could not find user with email: " + email);
	}
}
