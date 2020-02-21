package com.mec.aop.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class JDKProxy {

	static <T> T getProxy(Object object) {
		Class<?> klass = object.getClass();
		
		return getProxy(object, klass);
		
	}
	
	static <T> T getProxy(Class<?> klass) throws Exception {
		Object object = klass.newInstance();
		
		return getProxy(object, klass);
		
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getProxy(Object object, Class<?> klass) {
		return (T) Proxy.newProxyInstance(klass.getClassLoader(), klass.getInterfaces(), new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				return BeanFactory.getBeanById(object).invoke(method, args);
			}
		});
	}
	
}
