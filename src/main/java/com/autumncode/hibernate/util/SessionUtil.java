package com.autumncode.hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.log4testng.Logger;

public class SessionUtil {

	private static SessionUtil instance = new SessionUtil();
	private static SessionFactory factory;
	private static final String CONFIG_NAME = "/configuration.properties";
	
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private SessionUtil() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		
		factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	
	public static SessionUtil getInstance() {
		if(instance == null) {
			instance = new SessionUtil();
		}
		return instance;
	}
	
	public static Session getSession() {
		return factory.openSession();
	}
}




