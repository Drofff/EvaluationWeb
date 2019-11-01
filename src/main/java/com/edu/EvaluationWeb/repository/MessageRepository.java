package com.edu.EvaluationWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.EvaluationWeb.entity.Message;
import com.edu.EvaluationWeb.entity.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findBySender(User sender);

	@Query("select m from Message m join m.receivers r where :receiver in r")
	List<Message> findByReceiver(User receiver);

}