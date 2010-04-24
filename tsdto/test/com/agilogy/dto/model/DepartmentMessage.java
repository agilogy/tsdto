package com.agilogy.dto.model;

public interface DepartmentMessage {

	public String getName();

	public void setName(String name);

	public boolean hasName();

	public PersonMessage getDirector();

	public void setDirector(PersonMessage director);

	public boolean hasDirector();
}
