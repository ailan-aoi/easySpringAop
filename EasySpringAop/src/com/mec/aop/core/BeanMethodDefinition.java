package com.mec.aop.core;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

class BeanMethodDefinition {
	private Object object;
	private Method method;
	private List<MecParameter> parameters;
	
	public BeanMethodDefinition(Object obj, Method method) {
		this.object = obj;
		setMethod(method);
	}
	
	Object getObject() {
		return object;
	}
	
	void setObject(Object object) {
		this.object = object;
	}
	
	Method getMethod() {
		return method;
	}
	
	void setMethod(Method method) {
		this.method = method;
		
		parameters = new ArrayList<>();
		for (Parameter param : method.getParameters()) {
			parameters.add(new MecParameter(param));
		}
	}
	
	Object[] getParameterValues() {
		int length = parameters.size();
		Object[] params = new Object[length];
		
		for (int i = 0; i < length; i++) {
			params[i] = parameters.get(i).getValue();
		}
		
		return params;
	}
	
	MecParameter[] getParameters() {
		MecParameter[] params = new MecParameter[parameters.size()];
		
		return parameters.toArray(params);
	}
	
	void setParameters(List<MecParameter> parameters) {
		this.parameters = parameters;
	}
	
	
}
