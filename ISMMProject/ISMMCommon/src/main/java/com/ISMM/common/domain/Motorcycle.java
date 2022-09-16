package com.ISMM.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ISMM.common.model.VehicleEntity;

@Entity
@Table(name = "motorcycles")
public class Motorcycle extends VehicleEntity{
	
	@Column(length  = 25)
	private String finalDrive;
	
	@Column(length  = 25)
	private String coolingSystem;
	
	@Column(length  = 25)
	private String ignition;
	
	@Column(length  = 60)
	private String engineCapacity;

	public String getFinalDrive() {
		return finalDrive;
	}

	public void setFinalDrive(String finalDrive) {
		this.finalDrive = finalDrive;
	}

	public String getCoolingSystem() {
		return coolingSystem;
	}

	public void setCoolingSystem(String coolingSystem) {
		this.coolingSystem = coolingSystem;
	}

	public String getIgnition() {
		return ignition;
	}

	public void setIgnition(String ignition) {
		this.ignition = ignition;
	}

	public String getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(String engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
	
	
	
	
}
