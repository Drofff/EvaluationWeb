package com.drofff.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drofff.edu.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
