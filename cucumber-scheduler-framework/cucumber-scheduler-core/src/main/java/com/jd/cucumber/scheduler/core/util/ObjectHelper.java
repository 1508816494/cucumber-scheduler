package com.jd.cucumber.scheduler.core.util;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public abstract class ObjectHelper {

	private static final String TRANSIENT_ID_SUFFIX = "Id";

	public static boolean isEmpty(Object object) {
		return object == null || object.toString().trim().length() == 0;
	}

	public static <T> boolean isTransientId(Class<T> clazz, Field field) {
		if (!field.getName().endsWith(TRANSIENT_ID_SUFFIX)) {
			return false;
		}
		Method getMethod = ReflectHelper.getGetterMethod(clazz, field);
		if (getMethod != null && getMethod.getAnnotation(Transient.class) != null) {
			return true;
		}
		return false;
	}

}
