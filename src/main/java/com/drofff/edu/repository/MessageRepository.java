package com.drofff.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.drofff.edu.entity.Message;
import com.drofff.edu.entity.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

	List<Message> findBySender(User sender);

	@Query("select m from Message m join m.receivers r where :receiver in r")
	List<Message> findByReceiver(User receiver);

}