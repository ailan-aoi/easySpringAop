package com.mec.aop.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mec.aop.annotation.Autowired;
import com.mec.aop.annotation.Bean;
import com.mec.aop.annotation.Component;
import com.mec.aop.annotation.Qualifier;
import com.my.util.core.PackageScanner;
import com.my.util.core.TypeDeal;

public class BeanFactory {
	private static Map<String, BeanDefinition> beanFactory;
	private static Map<String, String> beanIdMap;
	private BeanMethodFacory bmf;
	static {
		beanFactory = new ConcurrentHashMap<>();
		beanIdMap = new ConcurrentHashMap<>();
	}
	
	
	public BeanFactory() {
		bmf = new BeanMethodFacory() {
			@Override
			void wired(BeanDefinition bd) {
				wired(bd);
			}
			@Override
			Map<String, BeanDefinition> getBeanFactory() {
				return beanFactory;
			}
		};
	}
	
	public void initBeanFactory(String packageName) {
		new PackageScanner() {
			@Override
			public void dealKlass(Class<?> klass) {
				if (klass.isPrimitive() || 
						klass.isInterface() || 
						klass.isEnum() || 
						klass.isAnnotation() ||
						!klass.isAnnotationPresent(Component.class)) {
					return;
				}
				String className = klass.getName();
				BeanDefinition bd = new BeanDefinition(klass);
				beanFactory.put(className, bd);

				Component component = klass.getAnnotation(Component.class);
				String id = component.id();
				if (!id.isEmpty()) {
					beanIdMap.put(id, className);
				}
				
				try {
					dealBeanMethod(klass, bd.getObject());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.packageScanner(packageName);
		
		bmf.scanPool();
	}
	
	private void dealBeanMethod(Class<?> klass, Object object) throws Exception{
		Method[] methods = klass.getDeclaredMethods();
		
		for(Method method : methods) {
			if (!method.isAnnotationPresent(Bean.class)) {
				continue;
			}
			Class<?> returnType = method.getReturnType();
			String className = returnType.getName();
			if (returnType.equals(void.class)) {
				throw new Exception("bean注解的使用不合适！！！");
			}
			Bean bean = method.getAnnotation(Bean.class);
			String id = bean.value();
			if (!id.isEmpty()) {
				beanIdMap.put(id, returnType.getName());
			}
			if (method.getParameterCount() <= 0) {
				Object res = method.invoke(object);
				beanFactory.put(className, new BeanDefinition(res));
				continue;
			}
			bmf.addMethod(className, new BeanMethodDefinition(klass.newInstance(), method));
		}
	}
	
	private void wired(BeanDefinition bd) throws Exception {
		if (bd.isDone()) {
			return;
		}
		Class<?> klass = bd.getKlass();
		Object obj = bd.getObject();
		Field[] fields = klass.getDeclaredFields();
		
		bd.setDone(true);
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Autowired.class)) {
				continue;
			}
			
			Autowired autowired = field.getAnnotation(Autowired.class);
			String value = autowired.value();
			if (!value.isEmpty()) {
				setFieldUserWired(klass, obj, field, value);
				continue;
			}
			setFieldAutoWired(klass, obj, field);
		}
	}
	
	private void setFieldUserWired(Class<?> klass,  Object object, Field field, String value) throws Exception {
		Object val = null;
		val = TypeDeal.StringToObject(field.getType(), value);
		if (val == null) {
			BeanDefinition bd = beanFactory.get(value);
			if (!bd.isDone()) {
				wired(bd);
			}
			val = bd.getObject();
		}
		setFieldCommon(klass, object, field, val);
	}
	
	private void setFieldAutoWired(Class<?> klass,  Object object, Field field) throws Exception {
		String className = field.getType().getName();
		BeanDefinition bd;
		if (field.isAnnotationPresent(Qualifier.class)) {
			String id = field.getAnnotation(Qualifier.class).value();
			bd = getBeanById(id);
		} else {
			bd = beanFactory.get(className);
		}
		
		if (!bd.isDone()) {
			wired(bd);
		}
		
		Object val = bd.getObject();
		setFieldCommon(klass, object, field, val);
	}
	
	private void setFieldCommon(Class<?> klass, Object object, Field field, Object val) throws IllegalArgumentException, IllegalAccessException {
		String fieldName = field.getName();
		String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
		try {
			Method method = klass.getMethod(methodName, field.getType());
			method.invoke(object, val);
		} catch (Exception e) {
			field.setAccessible(true);
			field.set(object, val);
		}
	}
	
	static BeanDefinition getBeanById(Object obj) throws Exception {
		String className = obj.getClass().getName();
		
		return getBeanById(className);
	}
	
	private static BeanDefinition getBeanById(String id) throws Exception{
		BeanDefinition bd = beanFactory.get(id);
		if (bd == null) {
			String className = beanIdMap.get(id);
			if (className == null) {
				throw new Exception("id" + id + "不存在");
			}
			bd = beanFactory.get(className);
			return bd;
		}
		
		return bd;
	}
	
	public void addMethodIntercepter(String className, MethodIntercepter methodInterceoter) {
		try {
			getBeanById(className).addIntercepter(methodInterceoter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(String id) throws Exception{
		BeanDefinition bd = getBeanById(id);
		
		wired(bd);
		
		return (T) bd.getProxy();
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getObject(Class<?> klass) throws Exception{
		String className = klass.getName();
		BeanDefinition bd = beanFactory.get(className);
		if (bd == null) {
			throw new Exception("class:" + klass + "不存在!!!");
		}
		
		wired(bd);
		
		return (T) bd.getProxy();
		
	}
}
