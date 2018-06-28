package chapter06.twotables;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import com.autumncode.hibernate.util.SessionUtil;

import chapter3.Employee;

public class PersistTest {

	@Test
	public void createTables() {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.getTransaction();
			tx.begin();
			
			Employee employee = new Employee();
			employee.setName("Yamazaki Shermie");
			employee.setSalary(4000.0);
			employee.setDepartament("Games");
			
			session.save(employee);
			
			tx.commit();
		}
	}
}
