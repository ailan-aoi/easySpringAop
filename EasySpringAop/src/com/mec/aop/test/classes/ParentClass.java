package com.mec.aop.test.classes;

import com.mec.aop.annotation.Component;

@Component
public class ParentClass {
	
	public ParentClass() {
	}

	public void doSomething() {
		System.out.println("这里做了事情");
	}

	public void doAnything() {
		System.out.println("这里做了其他事");
	}
	
}
