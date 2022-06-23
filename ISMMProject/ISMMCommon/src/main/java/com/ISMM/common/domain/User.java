package com.ISMM.common.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	//Passwords will be encrypted with a Password Encoder that has a length of 64
	@Column(length = 64, nullable = false)
	private String pasword;

	@Column(name = "first_name",length = 45, nullable = false)
	private String firstName;

	@Column(name = "last_name",length = 45, nullable = false)
	private String lastName;
	
	@Column(length = 64)
	private String photos;
	private Boolean enabled;
	
	@ManyToMany
	@JoinTable(
			name = "users_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
			)
	private Set<Role> userRoles = new HashSet<>();

	
	
	public User(String email, String pasword, String firstName, String lastName) {
		this.email = email;
		this.pasword = pasword;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User() {}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasword() {
		return pasword;
	}

	public void setPasword(String pasword) {
		this.pasword = pasword;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhotos() {
		return photos;
	}

	public void setPhotos(String photos) {
		this.photos = photos;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(Set<Role> userRoles) {
		this.userRoles = userRoles;
	}
	
	

}
