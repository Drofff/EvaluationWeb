package com.edu.EvaluationWeb.repository;

import com.edu.EvaluationWeb.entity.Question;
import com.edu.EvaluationWeb.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsRepository extends JpaRepository<Question, Long> {

    @Query(value = "select * from question where question_id = :test", nativeQuery = true)
    List<Question> findByTest(Test test);

}
