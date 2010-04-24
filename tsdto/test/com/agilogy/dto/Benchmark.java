package com.agilogy.dto;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

import com.agilogy.dto.model.PersonMessage;

/**
 * This class has some little benchmarks that I used in order to check what parts need optimization.
 * Keep in mind, though, that the goal of the framework is productivity, not efficiency. 
 */
public class Benchmark {

	static int RUNS = 100000;

	public static void main(String[] args) throws SecurityException,
			NoSuchMethodException {
		benchmarkDTO();
	}

	private static void benchmarkDTO() {
		PersonMessage staticPm = new PersonMessage() {
			Date birthDate;
			String name;

			@Override
			public Date getBirthDate() {
				return birthDate;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public boolean hasBirthDate() {
				return true;
			}

			@Override
			public boolean hasName() {
				return true;
			}

			@Override
			public void setBirthDate(Date birthDate) {
				this.birthDate = birthDate;
			}

			@Override
			public void setName(String name) {
				this.name = name;
			}
		};
		PersonMessage dynamicPm = DTOFactory.createEmpty(PersonMessage.class);
		int runs = 100000;
		long staticTime = exerciseGettersAndSetters(staticPm, runs);
		long dynamicTime = exerciseGettersAndSetters(dynamicPm, runs);
		System.out.println(" " + runs + "RUNS -> STATIC: " + staticTime
				+ " vs DYNAMIC:" + dynamicTime);
	}

	private static long exerciseGettersAndSetters(PersonMessage pm,
			int runs) {
		String name = "The name";
		Date date = new Date();
		long start = System.currentTimeMillis();
		for (int i = 0; i < runs; i++) {
			pm.setName(name);
			pm.setBirthDate(date);
			pm.getName();
			pm.getBirthDate();
		}
		long end = System.currentTimeMillis();
		return end - start;
	}

	@SuppressWarnings("unused")
	private static void benchMarkGetMethod() throws NoSuchMethodException {
		Method[] ma = PersonMessage.class.getMethods();
		Random r = new Random();
		long start = System.currentTimeMillis();
		for (int i = 0; i < RUNS; i++) {
			int idx = r.nextInt(ma.length);
			Method m = ma[idx];
			String methodName = ma[idx].getName();
			Method m2 = PersonMessage.class.getMethod(methodName, m
					.getParameterTypes());
		}
		long end = System.currentTimeMillis();
		System.out.println("" + RUNS + " runs in " + (end - start)
				+ " millis (" + (end - start) / RUNS + ")");
	}
}
