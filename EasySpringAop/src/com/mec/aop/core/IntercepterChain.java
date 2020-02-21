package com.mec.aop.core;

import java.lang.reflect.Method;

public class IntercepterChain {
	private IIntercepter intercepter;
	private IntercepterChain next;
	private static boolean goon;
	
	IntercepterChain(IIntercepter intercepter) {
		this.intercepter = intercepter;
		this.next = null;
	}
	
	void addIntercepter(IIntercepter intercepter) {
		IntercepterChain chain = new IntercepterChain(intercepter);
		
		chain.next = this.next;
		this.next = chain;
	}
	
	void addIntercepter(IntercepterChain chain) {
		
		chain.next = this.next;
		this.next = chain;
	}

	IIntercepter getIntercepter() {
		return intercepter;
	}

	IntercepterChain getNext() {
		return next;
	}
	
	static Object doChain(IntercepterChain chain, Object object, Method method, Object[] args) throws Throwable {
		Object result = null;
		if (chain == null) {
			result = method.invoke(object, args);
		} else {
			IIntercepter intercepter = chain.getIntercepter();
			if (intercepter != null && intercepter.hasMethod(method)) {
				if(goon = intercepter.before(args)) {
					try {
						result = doChain(chain.next, object, method, args);
						if (goon) {
							result = intercepter.after(result);
						}
					} catch (Throwable e) {
						intercepter.dealException(e);
					}
				} else {
					return result;
				}
			} else {
				result = doChain(chain.next, object, method, args);
			}
			
		}
		
		return result;
	}
}
