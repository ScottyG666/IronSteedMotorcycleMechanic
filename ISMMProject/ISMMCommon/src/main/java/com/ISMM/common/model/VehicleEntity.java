package com.ISMM.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class VehicleEntity extends IDBasedEntity{

	@Column(length = 17, nullable = false)
	private String vehicleIdentificationNumber;
	
	@Column(length = 25, nullable = false)
	private String manufacturer;

	@Column(length = 35, nullable = false)
	private String model;
	
	@Column(length = 4, nullable = false)
	private Integer manufacturedYear;

	@Column(length = 60)
	private String battery;
	
}
