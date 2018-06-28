package chapter03.simple;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Ranking {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private Person subject;
	
	@ManyToOne
	private Person observer;
	
	@ManyToOne
	private Skill skill;
	
	@Column
	private Integer ranking;
	
	public Ranking() {
		
	}
	
	public Long getId() {
		return this.id;
	}

	public Person getSubject() {
		return subject;
	}

	public void setSubject(Person subject) {
		this.subject = subject;
	}

	public Person getObserver() {
		return observer;
	}

	public void setObserver(Person observer) {
		this.observer = observer;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setSkill(Skill skill) {
		this.skill = skill;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	@Override
	public String toString() {
		return "Ranking [subject=" + subject + ", observer=" + observer + ", skill=" + skill + ", ranking=" + ranking
				+ "]";
	}
	
}
