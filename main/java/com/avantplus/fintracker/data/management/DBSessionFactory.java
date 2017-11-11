package com.avantplus.fintracker.data.management;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.avantplus.fintracker.data.entity.*;



public class DBSessionFactory {
	
	private static SessionFactory factory;
	
	public static SessionFactory getFactory() {
		if (factory==null) {
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
			configuration.addAnnotatedClass(PaymentType.class);
			configuration.addAnnotatedClass(UserTransactionBase.class);
			configuration.addAnnotatedClass(User.class);
			configuration.addAnnotatedClass(Category.class);
			configuration.addAnnotatedClass(Subcategory.class);
			configuration.addAnnotatedClass(PaymentType.class);
	        factory = configuration.buildSessionFactory();           
		}
		return factory;
	}

}
