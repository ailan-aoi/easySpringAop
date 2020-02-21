package com.mec.aop.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class BeanMethodFacory {
	private static final Map<String, BeanMethodDefinition> beanMethodPool;
	
	static {
		beanMethodPool = new ConcurrentHashMap<>();
	}
	
	public BeanMethodFacory() {
	}
	
	abstract Map<String, BeanDefinition> getBeanFactory();
	abstract void wired(BeanDefinition bd);
	
	void addMethod(String id, BeanMethodDefinition bmd) {
		if (beanMethodPool.containsKey(id)) {
			return;
		}
		
		beanMethodPool.put(id, bmd);
	}
	
	synchronized void scanPool() {
		boolean goon = true;
		while (goon) {
			goon = false;
			for (BeanMethodDefinition bmd : beanMethodPool.values()) {
				if (!checkParameters(bmd)) {
					continue;
				}
				try {
					Object obj = bmd.getMethod().invoke(bmd.getObject(), bmd.getParameterValues());
					String className = obj.getClass().getName();
					getBeanFactory().put(className, new BeanDefinition(obj));
					beanMethodPool.remove(className);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				goon = true;
			}
			if (beanMethodPool.size() <= 0) {
				return;
			}
		}
	}
	
	boolean checkParameters(BeanMethodDefinition bmd) {
		for (MecParameter param : bmd.getParameters()) {
			if (param.getValue() != null) {
				continue;
			}
			String id = param.getType().getName();
			BeanDefinition bd = getBeanFactory().get(id);
			if (bd == null) {
				return false;
			}
			wired(bd);
			
			param.setValue(bd.getObject());
		}
		
		return true;
	}
}
