package chapter04.broken;

import javax.persistence.*;

@Entity
public class Email {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Column
	String subject;
	
	@OneToOne
	//(mappedBy = "email")
	Message message;
	
	public Email() {
	}
	
	public Email(String subject) {
		setSubject(subject);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	
}
