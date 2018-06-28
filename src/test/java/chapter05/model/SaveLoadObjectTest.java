package chapter05.model;

import static org.testng.Assert.*;
import static org.testng.Assert.assertNotNull;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import com.autumncode.hibernate.util.SessionUtil;

import chapter04.model.SimpleObject;

public class SaveLoadObjectTest {
	
	//@Test
	public void testSaveLoad() {
		try (Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			SimpleObject obj = new SimpleObject();
			obj.setKey("sl");
			obj.setValue(10L);
			session.save(obj);

			assertNotNull(obj.getId());
			// we should have an id now, set by Session.save()
			Long id = obj.getId();
			tx.commit();
		}
	}
	
	//@Test
	public void testSavingEntitiesTwice() {
		Long id;
		SimpleObject obj;
	
		try(Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			
			obj = new SimpleObject();
			obj.setKey("osas");
			obj.setValue(10L);
			
			session.save(obj);
			
			assertNotNull(obj.getId());
			id = obj.getId();
			
			tx.commit();
		}
		
		try(Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			obj.setValue(12L);
			session.save(obj);
			tx.commit();
		}
	}
	
	//@Test
	public void testSavingOrUpdateEntitiesTwice() {
		Long id;
		SimpleObject obj;
	
		try(Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			
			obj = new SimpleObject();
			obj.setKey("osas2");
			obj.setValue(14L);
			
			session.save(obj);
			
			assertNotNull(obj.getId());
			
			id = obj.getId();
			
			tx.commit();
		}
		
		try(Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			
			obj.setValue(12L);
			session.saveOrUpdate(obj);
			
			tx.commit();
		}
		
		assertEquals(id, obj.getId());
	}
	
	@Test
	public void testSaveLoad2() {
		Long id = null;
		SimpleObject obj;
		
		try (Session session = SessionUtil.getSession()) {
			Transaction tx = session.beginTransaction();
			obj = new SimpleObject();
			obj.setKey("sl");
			obj.setValue(10L);
			session.save(obj);
			assertNotNull(obj.getId());
			// we should have an id now, set by Session.save()
			id = obj.getId();
			tx.commit();
		}
		
		try (Session session = SessionUtil.getSession()) {
			// we're loading the object by id
			SimpleObject o2 = session.load(SimpleObject.class, id);
			assertEquals(o2.getKey(), "sl");
			assertNotNull(o2.getValue());
			assertEquals(o2.getValue().longValue(), 10L);
			SimpleObject o3 = session.load(SimpleObject.class, id);
			// since o3 and o2 were loaded in the same session, they're not only
			// equivalent - as shown by equals() - but equal, as shown by ==.
			// since obj was NOT loaded in this session, it's equivalent but
			// not ==.
			assertEquals(o2, o3);
			assertEquals(obj, o2);
			assertTrue(o2 == o3);
			assertFalse(o2 == obj);
		}
	}
}






