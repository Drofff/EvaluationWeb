package com.drofff.edu.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Attendance {

	@Id
	private Long id;

	private LocalDateTime lastAttendance;

	public Attendance() {}

	public Attendance(Long id, LocalDateTime lastAttendance) {
		this.id = id;
		this.lastAttendance = lastAttendance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getLastAttendance() {
		return lastAttendance;
	}

	public void setLastAttendance(LocalDateTime lastAttendance) {
		this.lastAttendance = lastAttendance;
	}

}
