package chapter3.hibernate;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;

public class RankingTest {

	SessionFactory factory;
	
	@BeforeClass
	public void setup() {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure()
				.build();
		factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	public void testSaveRanking() {
		try(Session session = factory.openSession()){
			Transaction tx = session.beginTransaction();
			
			Person subject = savePerson(session, "J. C. Smell");
			Person observer = savePerson(session, "Drew Lombardo");
			
			Skill skill = saveSkill(session, "Java");
			
			Ranking ranking = new Ranking();
			ranking.setObserver(observer);
			ranking.setSubject(subject);
			ranking.setSkill(skill);
			ranking.setRanking(9);
			
			session.save(ranking);
			tx.commit();
		}
	}
	
	@Test
	public void testRankings() {
		populateRankingData();
		try(Session session = factory.openSession()){
			Transaction tx = session.beginTransaction();
			
			Query<Ranking> query = session.createQuery("FROM Ranking r WHERE r.subject.name=:name "
					+ "AND r.skill.name=:skill", Ranking.class);
			query.setParameter("name", "J. C. Smell");
			query.setParameter("skill", "Java");
			
			IntSummaryStatistics stats = query.list().stream().collect(Collectors.summarizingInt(Ranking::getRanking));
			
			long count = stats.getCount();
			int average = (int) stats.getAverage();
			
			tx.commit();
			session.close();
			assertEquals(count, 3);
			assertEquals(average, 7);
		}
	}
	
	private void populateRankingData() {
		 try(Session session = factory.openSession()) {
			 Transaction tx = session.beginTransaction();
			 createData(session, "J. C. Smell", "Gene Showrama", "Java", 6);
			 createData(session, "J. C. Smell", "Scottball Most", "Java", 7);
			 createData(session, "J. C. Smell", "Drew Lombardo", "Java", 8);
			 tx.commit();
		 }
	} 
	
	private void createData(Session session, String subjectName, String observerName,
			String skillName, int rank) {
		
		Person subject = savePerson(session, subjectName);
		Person observer = savePerson(session, observerName);
		Skill skill = saveSkill(session, skillName);
		
		Ranking ranking = new Ranking();
		ranking.setSubject(subject);
		ranking.setObserver(observer);
		ranking.setSkill(skill);
		ranking.setRanking(rank);
		session.save(ranking);
	}
	
	protected Person findPerson(Session session, String name) {
		Query<Person> query = session.createQuery("FROM Person p WHERE p.name=:name",
				Person.class);
		query.setParameter("name", name);
		Person person = query.uniqueResult();
		return person;
	}
	
	protected Person savePerson(Session session, String name) {
		Person person = findPerson(session, name);
		if(person == null) {
			person = new Person();
			person.setName(name);
			session.save(person);
		}
		return person;
	}
	
	public void changeRanking() {
		populateRankingData();
		try(Session session = factory.openSession()){
			Transaction tx = session.beginTransaction();
			Query<Ranking> query = session.createQuery("FROM Ranking r"
					+ "WHERE r.subject.name:=subjectName AND"
					+ "r.observer.name=:observerName AND"
					+ "r.skill.name:=skillName", Ranking.class);
			
			query.setParameter("subjectName", "J. C. Smell");
			query.setParameter("observerName", "Gene Showrama");
			query.setParameter("skillName", "Java");
			Ranking ranking = query.uniqueResult();
			assertNotNull(ranking, "Could not find matching ranking");
			ranking.setRanking(10);
			
			tx.commit();
		}
		
		
	}
	
	private Skill findSkill(Session session, String name) {
		Query<Skill> query = session.createQuery("FROM Skill s WHERE s.name=:name",
				Skill.class);
		query.setParameter("name", name);
		Skill skill = query.uniqueResult();
		return skill;
	}
	
	protected Skill saveSkill(Session session, String name) {
		Skill skill = findSkill(session, name);
		if(skill == null) {
			skill = new Skill();
			skill.setName(name);
			session.save(skill);	
		}
		return skill;
	}
	
	protected Ranking findRanking(Session session, String subjectName,
			String observerName, String skillName) {
		Query<Ranking> query = session.createQuery("from Ranking r where "
				+ "r.subject.name=:subjectName and "
				+ "r.observer.name=:observerName and "
				+ "r.skill.name=:skillName", Ranking.class);
		
		query.setParameter("subjectName", subjectName);
		query.setParameter("observerName", observerName);
		query.setParameter("skillName", skillName);
		
		Ranking ranking = query.uniqueResult();
		
		return ranking;
	}
	
	public void removeRanking() {
		try(Session session = factory.openSession()){
			session.beginTransaction();
			
			Ranking persistentRanking = findRanking(session, "J. C. Smell", "GeneShowrama", "Java");
			assertNotNull(persistentRanking, "Ranking not found.");
			session.delete(persistentRanking);
		
			session.getTransaction().commit();
		}
	}
	
}





