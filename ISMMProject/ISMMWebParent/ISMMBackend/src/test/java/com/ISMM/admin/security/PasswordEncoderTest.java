package com.ISMM.admin.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncodePassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "Bologna2022";
		
		String encodedPassword = passwordEncoder.encode(rawPassword);
		
		System.err.println(encodedPassword);
		
		boolean matched = passwordEncoder.matches(rawPassword, encodedPassword);
		
		assertThat(matched).isTrue();
	}
}
