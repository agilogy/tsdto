package com.agilogy.dto.model;

import java.util.Date;

import com.agilogy.dto.DTO;

public interface PersonMessage extends DTO {
	public String getName();
	public void setName(String name);
	public boolean hasName();
	
	public Date getBirthDate();
	public void setBirthDate(Date birthDate);	
	public boolean hasBirthDate();
}

