package com.agilogy.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.agilogy.dto.model.AllTheTypesDTO;
import com.agilogy.dto.model.Department;
import com.agilogy.dto.model.DepartmentMessage;
import com.agilogy.dto.model.Person;
import com.agilogy.dto.model.PersonMessage;

public class TestDTO {

	/**
	 * Test that we can create and empty DTO and call getters and setters
	 */
	@Test
	public void testPersonMessage() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);
		String name = "John Doe";
		Date birthDate = new Date();
		pm.setBirthDate(birthDate);
		pm.setName(name);

		assertEquals("name", name, pm.getName());
		assertEquals("birthDate", birthDate, pm.getBirthDate());
	}

	/**
	 * Test basic data types
	 */
	@Test
	public void testBasicTypes() {
		String aString = "Some random string";
		Date aDate = new Date();
		Integer anInteger = 5;
		Long aLong = 123l;
		Boolean aBoolean = true;

		AllTheTypesDTO dto = DTOFactory.createEmpty(AllTheTypesDTO.class);
		assertFalse(dto.hasAString());
		dto.setAString(aString);
		assertTrue(dto.hasAString());
		assertEquals(aString, dto.getAString());

		assertFalse(dto.hasADate());
		dto.setADate(aDate);
		assertTrue(dto.hasADate());
		assertEquals(aDate, dto.getADate());

		assertFalse(dto.hasAnInteger());
		dto.setAnInteger(anInteger);
		assertTrue(dto.hasAnInteger());
		assertEquals(anInteger, dto.getAnInteger());

		assertFalse(dto.hasALong());
		dto.setALong(aLong);
		assertTrue(dto.hasALong());
		assertEquals(aLong, dto.getALong());

		assertFalse(dto.hasABoolean());
		dto.setABoolean(aBoolean);
		assertTrue(dto.hasABoolean());
		assertEquals(aBoolean, dto.isABoolean());

	}

	/**
	 * DTOs have a feature that regular Java Beans don't have: They can tell
	 * wether a property has been set (even if it is to null) or not This test
	 * tests that this feature works
	 */
	@Test
	public void testWithNulls() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);
		pm.setBirthDate(null);

		assertTrue("Has birth date", pm.hasBirthDate());
		assertFalse("Does not have name", pm.hasName());
	}

	/**
	 * If we want to use the DTOs inside collections, it is important to make
	 * sure that equals and hashCode work as expected
	 */
	@Test
	public void testEqualsHashCode() {
		PersonMessage pm1 = DTOFactory.createEmpty(PersonMessage.class);
		PersonMessage pm2 = DTOFactory.createEmpty(PersonMessage.class);
		String name = "John Doe";
		Date birthDate = new Date();
		pm1.setBirthDate(birthDate);
		pm1.setName(name);
		pm2.setBirthDate(birthDate);
		pm2.setName(name);
		assertEquals(pm1, pm2);
		assertEquals(pm1.hashCode(), pm2.hashCode());
	}

	/**
	 * This test tests the nesting feature: A property value can be another DTO
	 */
	@Test
	public void testNestedObjects() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);
		String name = "John Doe";
		Date birthDate = new Date();
		pm.setBirthDate(birthDate);
		pm.setName(name);

		DepartmentMessage dm = DTOFactory.createEmpty(DepartmentMessage.class);
		String departmentName = "Sales";
		dm.setName(departmentName);
		dm.setDirector(pm);

		assertEquals(pm, dm.getDirector());
	}

	/**
	 * Seed a DTO from a regular Java Bean. For each property in the dto, the
	 * corresponding getter will be called in the java bean, the value collected
	 * and set in the DTO
	 */
	@Test
	public void testFromSimpleObject() {
		String name = "John Doe";
		String phoneNumber = "555123456";
		Person p = new Person(name, phoneNumber);
		PersonMessage pm = DTOFactory.createFromObject(PersonMessage.class, p);
		assertEquals(p.getName(), pm.getName());
		assertFalse(pm.hasBirthDate());
	}

	/**
	 * Here things get trickier: Sometimes, the source Java Beans will have
	 * associations, like Dept->Person. In this case, the DTOs may have
	 * associations as well but the types won't match. We expect the framework
	 * to detect these cases and automatically create a new DTO, seed it from
	 * the associated object and set it as the value
	 */
	@Test
	public void testAssociations() {
		String name = "John Doe";
		String phoneNumber = "555123456";
		Person p = new Person(name, phoneNumber);
		String deptName = "Sales";
		Department d = new Department(deptName, p);
		DepartmentMessage dm = DTOFactory.createFromObject(
				DepartmentMessage.class, d);
		assertEquals(dm.getName(), d.getName());
		assertEquals(dm.getDirector().getName(), d.getDirector().getName());
		assertFalse(dm.getDirector().hasBirthDate());
	}

	/**
	 * Test associations to N (Collections)
	 */
	@Test
	public void testAssociationsToN() {
		String name = "John Director";
		String phoneNumber = "555123456";
		Person director = new Person(name, phoneNumber);
		Set<Person> employees = new HashSet<Person>();
		Person employee1 = new Person("John Employee 1", phoneNumber);
		employees.add(employee1);
		Person employee2 = new Person("Jane Employee 2", phoneNumber);
		employees.add(employee2);
		Department d = new Department("Sales", director);
		d.setEmployees(employees);

		DepartmentMessage dm = DTOFactory.createFromObject(
				DepartmentMessage.class, d);
		assertNotNull(dm.getEmployees());
		assertEquals("There must be two employees", 2, dm.getEmployees().size());
		boolean[] found = new boolean[] { false, false };
		for (PersonMessage pm : dm.getEmployees()) {
			if (!found[0] && pm.getName().equals(employee1.getName())) {
				found[0] = true;
			} else if (!found[1] && pm.getName().equals(employee2.getName())) {
				found[1] = true;
			} else {
				fail("Unknwon or repeated employee was found" + pm);
			}
		}
		if (!found[0] || !found[1]) {
			fail("Some employee " + found + " was not found in "
					+ dm.getEmployees());
		}
	}
	/*
	 * //Right now, we don't need the marker interface, so we will try to get
	 * along without it interface WithoutMarker { void getName();
	 * 
	 * void setName(); }
	 * 
	 * interface WithMarker extends WithoutMarker, DTO {
	 * 
	 * }
	 * 
	 * @Test public void testCheckMarkerInterface() { try { WithoutMarker wm =
	 * DTOFactory.createEmpty(WithoutMarker.class);
	 * fail("Should have failed but returned this: " + wm); } catch
	 * (IllegalArgumentException e) { // OK } WithMarker wm =
	 * DTOFactory.createEmpty(WithMarker.class); }
	 */
}
