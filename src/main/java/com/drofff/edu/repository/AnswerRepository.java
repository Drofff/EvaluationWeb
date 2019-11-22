package com.drofff.edu.repository;

import com.drofff.edu.entity.Answer;
import com.drofff.edu.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(value = "select * from answer where answer_id = :question", nativeQuery = true)
    List<Answer> findByQuestion(Question question);

}
