package com.edu.EvaluationWeb.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String topic;

	private String text;

	private LocalDateTime dateTime;

	@ManyToMany
	@JoinTable(name = "message_receiver",
		joinColumns = @JoinColumn(name = "message_id"),
		inverseJoinColumns = @JoinColumn(name = "receiver_id")
	)
	private List<User> receivers;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	public void copyFieldValuesFrom(Message message) {
		topic = message.topic;
		text = message.text;
		dateTime = message.dateTime;
		receivers = message.receivers;
		sender = message.sender;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public List<User> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<User> receivers) {
		this.receivers = receivers;
	}
}
