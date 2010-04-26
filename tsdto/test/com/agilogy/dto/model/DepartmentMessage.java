package com.agilogy.dto.model;

import java.util.Set;

public interface DepartmentMessage {

	public String getName();

	public void setName(String name);

	public boolean hasName();

	public PersonMessage getDirector();

	public void setDirector(PersonMessage director);

	public boolean hasDirector();

	public Set<PersonMessage> getEmployees();

	public void setEmployees(Set<PersonMessage> value);
}
