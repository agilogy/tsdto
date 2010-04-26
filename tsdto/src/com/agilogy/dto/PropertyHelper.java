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

/**
 * This class implements the JavaBean conventions as understood by the framework
 */
public abstract class PropertyHelper {

	public static boolean isChecker(Method method) {
		return method.getName().startsWith("has") && method.getParameterTypes().length == 0
				&& !method.getName().equals("hashCode");
	}

	public static boolean isGetter(Method method) {
		return (method.getName().startsWith("get") && method.getParameterTypes().length == 0)
				|| (method.getName().startsWith("is") && method.getParameterTypes().length == 0);
	}

	public static boolean isSetter(Method method) {
		return method.getName().startsWith("set") && method.getParameterTypes().length == 1;
	}

	public static String getPropertyName(Method method) {
		String propName;
		if (method.getReturnType().equals(Boolean.class)) {
			propName = method.getName().substring(2);
		} else {
			propName = method.getName().substring(3);
		}
		return propName;
	}

	public static String getGetterNameForProperty(String propertyName) {
		return "get" + propertyName;
	}
}
