package chapter3.hibernate;

import org.testng.annotations.Test;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;

public class ModelTest {

	@Test
	public void testModelCreation() {
		Person subject = new Person();
		subject.setName("J. C. Smell");
		
		Person observer = new Person();
		observer.setName("Drew Lombardo");
		
		Skill skill = new Skill();
		skill.setName("Unix/Linux");
		
		Ranking ranking = new Ranking();
		ranking.setObserver(observer);
		ranking.setSubject(subject);
		ranking.setSkill(skill);
		ranking.setRanking(9);
		
		System.out.println(ranking);
	}
}
