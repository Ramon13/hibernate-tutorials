package chapter2.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import chapter02.hibernate.Message;

import static org.testng.Assert.assertEquals; 

public class PersistenceTest {

	SessionFactory factory;
	
	@BeforeSuite
	public void setup() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}
	
	@Test
	public void saveMessage() {
		Message message = new Message("Hello World!");
		try(Session session = factory.openSession()){
			Transaction tx = session.beginTransaction();
			session.persist(message);
			tx.commit();
		}
	}
	
	@Test(dependsOnMethods= "saveMessage")
	public void readMessage() {
		try(Session session = factory.openSession()){
			List<Message> list = session.createQuery("FROM Message", Message.class).list();
			
			assertEquals(list.size(), 1);
			for(Message m : list) {
				System.out.println(m);
			}
		}
	}
}











