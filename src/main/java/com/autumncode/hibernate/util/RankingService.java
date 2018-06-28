package com.autumncode.hibernate.util;

import java.util.Map;

import chapter03.simple.Person;

public interface RankingService {

	int getRankingFor(String subjectName, String skillName);
	
	void addRanking(String subjectName, String observerName, String skillName, int rankingLevel);
	
	void updateRanking(String subjectName, String observerName, String skillName, int rankingLevel);
	
	void removeRanking(String subjectName, String observerName, String skillName);
	
	Map<String, Integer> findRankingsFor(String subjectName);
	
	Person findBestPersonFor(String skillName);
}
