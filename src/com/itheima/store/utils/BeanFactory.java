package com.itheima.store.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BeanFactory {
	
	/*public static UserDao getUserDao(){
		return new UserDaoImpl();
	}
	
	public static ProductDao getProductDao(){
		return new ProductDaoImpl();
	}*/
	
	public static Object getBean(String id){
		
		SAXReader reader = new SAXReader();
		try {
			// 解析XML  :dom4j
			Document document = reader.read(BeanFactory.class.getClassLoader().getResourceAsStream("applicationContext.xml"));
			// 获得class中的内容.
			Element beanElement = (Element) document.selectSingleNode("//bean[@id='"+id+"']");
			String value = beanElement.attributeValue("class");
			// System.out.println(value);
			// 反射生成实例 :
			Class clazz = Class.forName(value); // 没有增强的class:
			final Object obj = clazz.newInstance();
			
			if(id.endsWith("Dao")){
				Object objProxy = Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(), new InvocationHandler() {
					
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if(method.getName().startsWith("save")){
							System.out.println("权限校验=================");
							return method.invoke(obj, args);
						}
						
						return method.invoke(obj, args);
					}
				});
				return objProxy;
			}
			
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args) {
		BeanFactory.getBean("productDao");
	}
}
