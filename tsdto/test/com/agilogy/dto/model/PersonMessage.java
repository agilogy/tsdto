package com.agilogy.dto.model;

import java.util.Date;

public interface PersonMessage {
	public String getName();

	public void setName(String name);

	public boolean hasName();

	public Date getBirthDate();

	public void setBirthDate(Date birthDate);

	public boolean hasBirthDate();
}
