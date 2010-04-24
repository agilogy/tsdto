package com.agilogy.dto;

import java.lang.reflect.Method;

/**
 * This class implements the JavaBean conventions as they are understood by the framework
 * @author jose
 *
 */
public abstract class PropertyHelper {

	public static boolean isChecker(Method method) {
		return method.getName().startsWith("has")
				&& method.getParameterTypes().length == 0
				&& !method.getName().equals("hashCode");
	}

	public static boolean isGetter(Method method) {
		return method.getName().startsWith("get")
				&& method.getParameterTypes().length == 0;
	}

	public static boolean isSetter(Method method) {
		return method.getName().startsWith("set")
				&& method.getParameterTypes().length == 1;
	}
}
