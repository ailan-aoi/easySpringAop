package com.mec.aop.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class MethodIntercepter extends IntercepterAdepter{
	private List<Method> methods;
	
	private MethodIntercepter() {
		methods = new ArrayList<>();
	}
	
	public MethodIntercepter(Method method) {
		this();
		addMethod(method);
	}
	
	public MethodIntercepter(Method[] methods) {
		this();
		for (Method method : methods) {
			this.methods.add(method);
		}
	}

	public void addMethod(Method method) {
		if (methods.contains(method)) {
			return;
		}
		methods.add(method);
	}
	
	@Override
	public boolean hasMethod(Method method) {
		return methods.contains(method);
	}
	
	
}
