package com.ISMM.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ISMM.admin.security.service.ISMMUserDetailsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Bean
	public UserDetailsService userDetailsService () {
		return new ISMMUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}
	
	
	/**
	 * 
	 * 
	 * @return A {@linkplain DaoAuthenticationProvider} Object with the UserDetailsService assigned to an instance of 
	 * 			ISMMUserDetailsService and the PasswordEncoder assigned to a BCryptPasswordEncoder.
	 * 			
	 */
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	/**
	 * Passing the custom created authenticationProvider() created above into the {@linkplain AuthenticationManagerBuilder}
	 * 		as the authenticationProvider
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	/**
	 * 	Authentication configuration for access to the Admin Control panel. The form submitted
	 * 		via the .loginPage with a designated input fields assigned with names.
	 * 		-Email designated as the Username field for authentication
	 * 
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and()
				.logout()
				.permitAll()
			.and()
				.rememberMe()
				.key("AbcDefgHijklmnOpqrs_1234567890")
				//Reduces the token validity from 2 weeks (default) to one week 
				//	(7 days * 24 hours * 60 minutes * 60 seconds)
				.tokenValiditySeconds(7 * 24 * 60 * 60);
	}

	
	/**
	 * Allows access to these specified folders without authenticating the user, IE: the Login page
	 * 	 still able to be styalized.
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**" , "/webjars/**", "/css/**" );
	}
	
	

	
	
}
