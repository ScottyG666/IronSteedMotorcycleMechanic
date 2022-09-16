package com.ISMM.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person extends IDBasedEntity{
	
	@Column(length = 64, nullable = false, unique = true)
	private String email;
	
	@Column(name = "first_name", nullable = false,length = 64)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 64)
	private String lastName;
	
	public Person(String email, String fName, String lName) {
		this.email = email;
		this.firstName = fName;
		this.lastName = lName;
	}

	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	

	
}
