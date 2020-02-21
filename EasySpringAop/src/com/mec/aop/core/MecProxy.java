package com.mec.aop.core;

public class MecProxy {
	private static final int JDK_PROXY = 0;
	private static final int CGLIB_PROXY = 1;

	private int proxyType;
	
	public MecProxy() {
		proxyType = JDK_PROXY;
	}

	public int getProxyType() {
		return proxyType;
	}

	public void setProxyType(int proxyType) {
		this.proxyType = proxyType;
	}
	
	public <T> T getProxy(Object object) {
		if (proxyType == CGLIB_PROXY || object.getClass().getInterfaces().length == 0) {
			return CGLibProxy.getProxy(object);
		}
		return JDKProxy.getProxy(object);
	}
	
	public <T> T getProxy(Class<?> klass) throws Exception{
		if (proxyType == CGLIB_PROXY || klass.getInterfaces().length == 0) {
			return CGLibProxy.getProxy(klass);
		}
		return JDKProxy.getProxy(klass);
	}
}
