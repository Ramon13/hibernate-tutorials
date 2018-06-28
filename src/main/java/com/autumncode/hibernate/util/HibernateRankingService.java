package com.autumncode.hibernate.util;

import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import chapter03.simple.Person;
import chapter03.simple.Ranking;
import chapter03.simple.Skill;
import chapter3.hibernate.RankingTest;

public class HibernateRankingService extends RankingTest implements RankingService {

	@Override
	public int getRankingFor(String subjectName, String skillName) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			int average = getRankingFor(session, subjectName, skillName);
		
			tx.commit();
			return average;
		}
		
	}

	@Override
	public void addRanking(String subjectName, String observerName, String skillName, int rankingLevel) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			
			this.addRanking(session, subjectName, observerName, skillName, rankingLevel);
			
			tx.commit();
		}
	}

	private void addRanking(Session session, String subjectName,
			String observerName, String skillName, int rankingLevel) {
		
		Person subject = savePerson(session, subjectName);
		Person observer = savePerson(session, observerName);
		Skill skill = saveSkill(session, skillName);
		
		Ranking ranking = new Ranking();
		ranking.setSubject(subject);
		ranking.setObserver(observer);
		ranking.setSkill(skill);
		ranking.setRanking(rankingLevel);
		
		session.save(ranking);

	}
	
	private int getRankingFor(Session session, String subjectName, String skillName) {
		Query<Ranking> query = session.createQuery("FROM Ranking r WHERE r.subject.name=:name "
				+ "AND r.skill.name=:skill", Ranking.class);
		query.setParameter("name", subjectName);
		query.setParameter("skill", skillName);
		
		IntSummaryStatistics stats = query.list().stream().collect(Collectors.summarizingInt(Ranking::getRanking));		
		
		int average = (int) stats.getAverage();
		return average;
	}

	@Override
	public void updateRanking(String subjectName, String observerName, String skillName, int rankingLevel) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			
			Ranking ranking = findRanking(session, subjectName, observerName, skillName);
			if(ranking == null) {
				addRanking(session, subjectName, observerName, skillName, rankingLevel);
			}else {
				ranking.setRanking(rankingLevel);
			}
			
			tx.commit();
		}
	}

	@Override
	public void removeRanking(String subjectName, String observerName, String skillName) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			
			Query<Ranking> query = session.createQuery("from Ranking r where "
					+ "r.subject.name=:subject and "
					+ "r.observer.name=:observer and "
					+ "r.skill.name=:skill", Ranking.class);

			query.setParameter("subject", subjectName);
			query.setParameter("observer", observerName);
			query.setParameter("skill", skillName);
			
			Ranking r = query.uniqueResult();
			
			session.delete(r);
			
			tx.commit();
		}
	}
	
	private Map<String, Integer> findRankingFor(Session session, String subject){
		Map<String, Integer> results = new HashMap<>();
		
		Query<Ranking> query = session.createQuery("from Ranking r where"
				+ "r.subject.name=: subject", Ranking.class);
		query.setParameter("subject", subject);
		
		List<Ranking> rankings = query.list();
		
		String lastSkillName="";
		int skillRankingSum = 0;
		int count = 0;
		
		for(Ranking ranking : rankings) {
			if(!lastSkillName.equals(ranking.getSkill().getName())) {
				skillRankingSum = 0;
				count = 0;
				lastSkillName = ranking.getSkill().getName();
			}
			
			skillRankingSum += ranking.getRanking();
			count++;
			results.put(lastSkillName, skillRankingSum/count);
		}
		
		return results;
	}

	@Override
	public Map<String, Integer> findRankingsFor(String subjectName) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			
			Map<String, Integer> results = findRankingFor(session, subjectName);
			
			tx.commit();
			
			return results;
		}
	}
	
	private Person findBestPersonFor(Session session, String skillName) {
		Query<Object[]> query = session.createQuery("select r.subject.name, avg(r.ranking) "
				+ "from Ranking r where "
				+ "r.skill.name=:skillName "
				+ "group by r.subject.name "
				+ "order by avg(r.ranking) desc", Object[].class);
		query.setParameter("skillName", skillName);
		
		List<Object[]> result = query.list();
		if(result.size() > 0) {
			return findPerson(session, (String) result.get(0)[0]);
		}
		
		return null;
	}

	@Override
	public Person findBestPersonFor(String skillName) {
		try(Session session = SessionUtil.getSession()){
			Transaction tx = session.beginTransaction();
			
			Person person = findBestPersonFor(session, skillName);
			
			tx.commit();
			
			return person;
		}
	}
}
















