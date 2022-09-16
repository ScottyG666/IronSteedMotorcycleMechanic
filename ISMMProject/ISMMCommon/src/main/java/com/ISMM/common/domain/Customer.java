package com.ISMM.common.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.ISMM.common.model.Person;

@Entity
@Table(name = "customers")
public class Customer extends Person{
	
	@Column(nullable = false, length = 64)
	private String password;
	

	private boolean enabled;

	public Customer(String email, String fName, String lName) {
		super(email, fName, lName);
		
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "customers_motorcycles",
			joinColumns = @JoinColumn(name = "customer_id"),
			inverseJoinColumns = @JoinColumn(name = "motorcycle_id"))
	private Set<Motorcycle> motorcycles = new HashSet<>();
	
}
