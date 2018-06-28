package com.autumncode.hibernate.util;

import static org.testng.Assert.assertNotNull;

import org.hibernate.Session;
import org.testng.annotations.Test;

public class TestSessionUtil {

	@Test
	public void testSessionFactory() {
		try(Session session = SessionUtil.getSession()){
			assertNotNull(session);
		}
	}
}
