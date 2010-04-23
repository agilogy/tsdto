package test.com.agilogy.dto;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import test.com.agilogy.dto.model.DepartmentMessage;
import test.com.agilogy.dto.model.Person;
import test.com.agilogy.dto.model.PersonMessage;

import com.agilogy.dto.DTOFactory;

public class TestDTO {

	@Test
	public void testPersonMessage() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);		
		String name ="John Doe";
		Date birthDate =  new Date();
		pm.setBirthDate(birthDate);
		pm.setName(name);
		
		assertEquals("name", name, pm.getName());
		assertEquals("birthDate", birthDate, pm.getBirthDate());
	}
	
	@Test
	public void testWithNulls() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);
		pm.setBirthDate(null);
		
		assertTrue("Has birth date", pm.hasBirthDate());
		assertFalse("Does not have name", pm.hasName());
	}
	
	@Test
	public void testEqualsHashCode() {
		PersonMessage pm1 = DTOFactory.createEmpty(PersonMessage.class);
		PersonMessage pm2 = DTOFactory.createEmpty(PersonMessage.class);
		String name ="John Doe";
		Date birthDate =  new Date();
		pm1.setBirthDate(birthDate);
		pm1.setName(name);
		pm2.setBirthDate(birthDate);
		pm2.setName(name);
		assertEquals(pm1, pm2);
		assertEquals(pm1.hashCode(), pm2.hashCode());
	}
	@Test
	public void testNestedObjects() {
		PersonMessage pm = DTOFactory.createEmpty(PersonMessage.class);		
		String name ="John Doe";
		Date birthDate =  new Date();
		pm.setBirthDate(birthDate);
		pm.setName(name);
		
		DepartmentMessage dm = DTOFactory.createEmpty(DepartmentMessage.class);
		String departmentName = "Sales";
		dm.setName(departmentName);
		dm.setDirector(pm);
		
		assertEquals(pm, dm.getDirector());
	}
	
	@Test
	public void testFromSimpleObject() {
		String name ="John Doe";
		String phoneNumber = "555123456";
		Person p = new Person(name, phoneNumber);
		PersonMessage pm = DTOFactory.createFromObject(PersonMessage.class, p);
		assertEquals(p.getName(), pm.getName());
		assertFalse(pm.hasBirthDate());
	}
}
