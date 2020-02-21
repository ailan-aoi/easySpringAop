package com.mec.aop.core;

import java.lang.reflect.Method;

public interface IIntercepter {
	boolean before(Object[] params);
	Object after(Object result);
	void dealException(Throwable e) throws Throwable;
	boolean hasMethod(Method method);
}
