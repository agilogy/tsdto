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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DTOInvocationHandler implements java.lang.reflect.InvocationHandler {
	final String NULL = "null";

	Map<String, Object> values;
	// key=property name, value = method
	Map<String, Method> getters;
	Map<String, Method> setters;
	Map<String, Method> checkers;

	Class<?> type;

	public DTOInvocationHandler(Class<?> type) {
		super();
		this.type = type;
		this.values = new HashMap<String, Object>();
		this.getters = new HashMap<String, Method>();
		this.setters = new HashMap<String, Method>();
		this.checkers = new HashMap<String, Method>();
		Method[] methods = type.getMethods();
		for (Method method : methods) {
			if (PropertyHelper.isGetter(method)) {
				getters.put(PropertyHelper.getPropertyName(method), method);
			} else if (PropertyHelper.isChecker(method)) {
				checkers.put(PropertyHelper.getPropertyName(method), method);
			} else if (PropertyHelper.isSetter(method)) {
				setters.put(PropertyHelper.getPropertyName(method), method);
			}
		}
	}

	@SuppressWarnings("unchecked")
	void seedFromObject(Object source) {
		if (source == null) {
			return;
		}

		for (String propertyName : setters.keySet()) {
			try {
				Method getter = source.getClass().getMethod(PropertyHelper.getGetterNameForProperty(propertyName));
				Object value = getter.invoke(source);
				Class<?> propertyType = getters.get(propertyName).getReturnType();
				if (Collection.class.isAssignableFrom(propertyType) && value != null && value instanceof Collection<?>) {
					Class<?> collectionClass = value.getClass();
					Collection<Object> valueToSet = (Collection<Object>) collectionClass.newInstance();
					ParameterizedType paramGenericType = (ParameterizedType) setters.get(propertyName)
							.getGenericParameterTypes()[0];
					Class<?> valueType = (Class<?>) paramGenericType.getActualTypeArguments()[0];
					for (Object element : (Collection<?>) value) {
						value = DTOFactory.createFromObject(valueType, element);
						valueToSet.add(value);
					}
					setProperty(propertyName, valueToSet);
				} else if (value == null || propertyType.isAssignableFrom(value.getClass())) {
					setProperty(propertyName, value);

				} else {
					// Classes don't match: We will assume the following
					// scenario:
					// We are creating a DTO and we have a field that needs to
					// be a DTO as well
					// We will create a DTO from the getter value and store it
					// as the value instead of the original one
					value = DTOFactory.createFromObject(propertyType, value);
					setProperty(propertyName, value);
				}
			} catch (SecurityException e) {
				throw new RuntimeException("Error finding getter. Is it not public?", e);
			} catch (NoSuchMethodException e) {
				// We can't initialize this property from the object, no problem
			} catch (Exception e) {
				throw new RuntimeException("Error invoking getter for property " + propertyName + " on object "
						+ source, e);
			}
		}

	}

	private void setProperty(String propertyName, Object value) {
		if (value == null) {
			value = NULL;
		}
		values.put(propertyName, value);
	}

	private Object getProperty(String propertyName) {
		Object result = values.get(propertyName);
		if (NULL == result) {
			result = null;
		}
		return result;
	}

	private boolean checkProperty(String propertyName) {
		return values.get(propertyName) != null;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		if (PropertyHelper.isSetter(method)) {
			setProperty(PropertyHelper.getPropertyName(method), args[0]);
		} else if (PropertyHelper.isGetter(method)) {
			return getProperty(PropertyHelper.getPropertyName(method));
		} else if (PropertyHelper.isChecker(method)) {
			return checkProperty(PropertyHelper.getPropertyName(method));
		} else if (isEquals(method)) {
			return isEqual(args[0]);
		} else if (isHashCode(method)) {
			return values.hashCode();
		} else if (isToString(method)) {
			return values.toString();
		}
		// Raise an exception ?
		return null;
	}

	private boolean isEqual(Object other) {
		if (!(other instanceof Proxy)) {
			return false;
		}
		InvocationHandler theOtherInvocationHandler = Proxy.getInvocationHandler(other);
		if (!(theOtherInvocationHandler instanceof DTOInvocationHandler)) {
			return false;
		}
		DTOInvocationHandler odiv = (DTOInvocationHandler) theOtherInvocationHandler;
		return odiv.type.isAssignableFrom(this.type) && odiv.values.equals(this.values);
	}

	private boolean isHashCode(Method method) {
		return method.getName().equals("hashCode") && method.getParameterTypes().length == 0;
	}

	private boolean isEquals(Method method) {
		return method.getName().equals("equals") && method.getParameterTypes().length == 1;
	}

	private boolean isToString(Method method) {
		return method.getName().equals("toString") && method.getParameterTypes().length == 0;
	}
}
