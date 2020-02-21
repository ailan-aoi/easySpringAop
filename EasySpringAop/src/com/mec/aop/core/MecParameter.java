package com.mec.aop.core;

import java.lang.reflect.Parameter;

class MecParameter {
	private Class<?> type;
	private Object value;
	
	public MecParameter(Parameter param) {
		type = param.getType();
		value = null;
	}

	Class<?> getType() {
		return type;
	}

	Object getValue() {
		return value;
	}
	
	void setValue(Object value) {
		this.value = value;
	}
}
