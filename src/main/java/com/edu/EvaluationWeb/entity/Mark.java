package com.edu.EvaluationWeb.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Mark {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long mark;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Profile student;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	private LocalDateTime localDateTime;

	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMark() {
		return mark;
	}

	public void setMark(Long mark) {
		this.mark = mark;
	}

	public Profile getStudent() {
		return student;
	}

	public void setStudent(Profile student) {
		this.student = student;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}