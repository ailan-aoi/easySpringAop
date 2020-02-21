package com.mec.aop.core;

import java.lang.reflect.Method;

class IntercepterAdepter implements IIntercepter {

	@Override
	public boolean before(Object[] params) {
		return true;
	}

	@Override
	public Object after(Object result) {
		return null;
	}

	@Override
	public void dealException(Throwable e) throws Throwable {
	}

	@Override
	public boolean hasMethod(Method method) {
		return false;
	}

}
