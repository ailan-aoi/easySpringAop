package com.mec.aop.core;

import java.lang.reflect.Method;

class BeanDefinition {
	private Class<?> klass;
	private ProxyInvoker proxyInvoker;
	private boolean done;
	
	private static final MecProxy mecProxy = new MecProxy();
	
	BeanDefinition(Class<?> klass) {
		try {
			Object object = klass.newInstance();
			Object proxy = mecProxy.getProxy(klass);
			this.klass = klass;
			this.proxyInvoker = new ProxyInvoker(object, proxy);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	BeanDefinition(Object obj) {
		Object proxy = new MecProxy().getProxy(obj);
		this.klass = obj.getClass();
		this.proxyInvoker = new ProxyInvoker(obj, proxy);
	}
	
	Class<?> getKlass() {
		return klass;
	}
	
	void setKlass(Class<?> klass) {
		this.klass = klass;
	}
	
	boolean isDone() {
		return done;
	}
	
	void setDone(boolean isDone) {
		this.done = isDone;
	}

	ProxyInvoker getProxyInvoker() {
		return proxyInvoker;
	}

	Object getObject() {
		return proxyInvoker.getObj();
	}
	
	Object getProxy() {
		return proxyInvoker.getProxy();
	}
	
	void setProxyInvoker(ProxyInvoker proxyInvoker) {
		this.proxyInvoker = proxyInvoker;
	}

	Object invoke(Method method, Object[] args) {
		try {
			return proxyInvoker.invoke(method, args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	void addIntercepter(MethodIntercepter intercepter) {
		proxyInvoker.addIntercepter(intercepter);
	}
	
	@Override
	public String toString() {
		return "BeanDefinition [klass=" + klass + ", done=" + done + "]";
	}
	
	
}
