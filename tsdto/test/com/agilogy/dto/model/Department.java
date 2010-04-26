package com.agilogy.dto.model;

import java.util.Set;

public class Department {

	private String name;
	private Person director;
	private Set<Person> employees;

	public Department() {
		super();
	}

	public Department(String name, Person director) {
		super();
		this.name = name;
		this.director = director;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getDirector() {
		return director;
	}

	public void setDirector(Person director) {
		this.director = director;
	}

	public Set<Person> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Person> employees) {
		this.employees = employees;
	}

}
