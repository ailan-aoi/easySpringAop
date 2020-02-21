package com.mec.aop.core;

import java.lang.reflect.Method;

class ProxyInvoker {
	private Object obj;
	private Object proxy;
	private IntercepterChain chain;
	
	ProxyInvoker(Object obj, Object proxy) {
		this.obj = obj;
		this.proxy = proxy;
		chain = new IntercepterChain(null);
	}
	
	Object getObj() {
		return obj;
	}

	Object getProxy() {
		return proxy;
	}

	IntercepterChain getChain() {
		return chain;
	}

	void addIntercepter(MethodIntercepter intercepter) {
		chain.addIntercepter(new IntercepterChain(intercepter));
	}
	
	Object invoke(Method method, Object[] args) throws Throwable{
		return IntercepterChain.doChain(chain, obj, method, args);
	}

	
}
