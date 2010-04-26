/**
 * Copyright (c) 2010, Agilogy
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions  are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *  	this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *  	this list of conditions and the following disclaimer in the documentation 
 *  	and/or other materials provided with the distribution.
 *  * Neither the name of Agilogy nor the names of its contributors may be used 
 *  	to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.agilogy.dto;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Random;

import com.agilogy.dto.model.PersonMessage;

/**
 * This class has some little benchmarks that I used in order to check what
 * parts need optimization. Keep in mind, though, that the goal of the framework
 * is productivity, not efficiency.
 */
public class Benchmark {

	static int RUNS = 100000;

	public static void main(String[] args) throws SecurityException, NoSuchMethodException {
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
		System.out.println(" " + runs + "RUNS -> STATIC: " + staticTime + " vs DYNAMIC:" + dynamicTime);
	}

	private static long exerciseGettersAndSetters(PersonMessage pm, int runs) {
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
			Method m2 = PersonMessage.class.getMethod(methodName, m.getParameterTypes());
		}
		long end = System.currentTimeMillis();
		System.out.println("" + RUNS + " runs in " + (end - start) + " millis (" + (end - start) / RUNS + ")");
	}
}
