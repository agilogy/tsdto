package com.agilogy.dto.model;

import java.util.Date;

import com.agilogy.dto.DTO;

public interface AllTheTypesDTO extends DTO{
	public String getAString();

	public void setAString(String value);

	public boolean hasAString();

	public Integer getAnInteger();

	public void setAnInteger(Integer value);

	public boolean hasAnInteger();

	public Date getADate();

	public void setADate(Date value);

	public boolean hasADate();

	public Long getALong();

	public void setALong(Long value);

	public boolean hasALong();

	public Boolean isABoolean();

	public void setABoolean(Boolean value);

	public boolean hasABoolean();
}
