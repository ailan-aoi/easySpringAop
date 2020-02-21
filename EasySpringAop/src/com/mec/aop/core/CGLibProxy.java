package com.mec.aop.core;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

class CGLibProxy {
	
	public CGLibProxy() {
	}
	
	static <T> T getProxy(Object object) {
		Class<?> klass = object.getClass();
		
		return getProxy(klass, object);
	}
	
	static <T> T getProxy(Class<?> klass) throws Exception{
		Object object = klass.newInstance();
		
		return getProxy(klass, object);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> T getProxy(Class<?> klass, Object object) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(klass);
		enhancer.setCallback(new MethodInterceptor() {
			
			@Override
			public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
				return BeanFactory.getBeanById(object).invoke(method, args);
			}
		});
		
		return (T) enhancer.create();
	}
	
}
