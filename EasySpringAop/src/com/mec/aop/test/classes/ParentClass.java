package com.mec.aop.test.classes;

import com.mec.aop.annotation.Component;

@Component
public class ParentClass {
	
	public ParentClass() {
	}

	public void doSomething() {
		System.out.println("������������");
	}

	public void doAnything() {
		System.out.println("��������������");
	}
	
}
