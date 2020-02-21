package com.mec.aop.test;

import java.lang.reflect.Method;

import com.mec.aop.core.BeanFactory;
import com.mec.aop.core.MethodIntercepter;
import com.mec.aop.test.classes.ParentClass;

public class Test {
	
	public static void main(String[] args) {
		BeanFactory bf = new BeanFactory();
		bf.initBeanFactory("com.mec.aop.test");
		
		
		Method method;
		try {
			method = Class.forName("com.mec.aop.test.classes.ParentClass").getMethod("doSomething");
			bf.addMethodIntercepter("com.mec.aop.test.classes.ParentClass", new MethodIntercepter(method) {

				@Override
				public boolean before(Object[] params) {
					System.out.println("1:先做了些事情");
					return true;
				}

				@Override
				public Object after(Object result) {
					System.out.println("1:后做了事情");
					return result;
				}
				
			});
			method = Class.forName("com.mec.aop.test.classes.ParentClass").getMethod("doAnything");
			bf.addMethodIntercepter("com.mec.aop.test.classes.ParentClass", new MethodIntercepter(method) {

				@Override
				public boolean before(Object[] params) {
					System.out.println("2:先做了些事情");
					return true;
				}

				@Override
				public Object after(Object result) {
					System.out.println("2:后做了事情");
					return result;
				}
				
			});
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try {
			ParentClass pf = bf.getObject("com.mec.aop.test.classes.ParentClass");
			pf.doSomething();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
