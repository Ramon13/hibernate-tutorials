package chapter04.mapped;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import com.autumncode.hibernate.util.SessionUtil;

import chapter04.model.SimpleObject;

public class RelationshipTest {

	//@Test()
	public void testImpliedInversionCode() {
		Long emailId;
		Long messageId;
		Email email;
		Message message;
		
		try (Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			
			email = new Email("Inverse Email");
			message = new Message("Inverse Message");
			// email.setMessage(message);
			message.setEmail(email);
			
			session.save(email);
			session.save(message);
			
			emailId = email.getId();
			messageId = message.getId();
			tx.commit();
		}
		
		assertEquals(email.getSubject(), "Inverse Email");
		assertEquals(message.getContent(), "Inverse Message");
		assertNull(email.getMessage());
		assertNotNull(message.getEmail());
		
		try (Session session = SessionUtil.getSession()) {
			email = session.get(Email.class, emailId);
			System.out.println(email);
			message = session.get(Message.class, messageId);
			System.out.println(message);
		}
			
		assertNotNull(email.getMessage());
		assertNotNull(message.getEmail());
		
	}
	
	@Test
	public void testMerge() {
		
		Long id;
		try (Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			SimpleObject simpleObject = new SimpleObject();
			simpleObject.setKey("testMerge");
			simpleObject.setValue(1L);
			session.save(simpleObject);
			id = simpleObject.getId();
			tx.commit();
		}
		
		SimpleObject so = validateSimpleObject(id, 1L);
		so.setValue(2L);
		
		try (Session session = SessionUtil.getSession()) {
			// merge is potentially an update, so we need a TX
			Transaction tx = session.beginTransaction();
			session.merge(so);
			tx.commit();
		}
		validateSimpleObject(id, 2L);
	}
	
	private SimpleObject validateSimpleObject(Long id, Long value) {
		SimpleObject so = null;
		try (Session session = SessionUtil.getSession()) {
			so = session.load(SimpleObject.class, id);
			assertEquals(so.getKey(), "testMerge");
			assertEquals(so.getValue(), value);
		}
		return so;
	}
}
	
	
	
	
	
