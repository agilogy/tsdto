package com.agilogy.dto.model;

import com.agilogy.dto.DTO;


public interface DepartmentMessage extends DTO{

	public String getName();
	public void setName(String name);
	public boolean hasName();
	
	public PersonMessage getDirector();
	public void setDirector(PersonMessage director);
	public boolean hasDirector();
}
