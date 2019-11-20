package com.edu.EvaluationWeb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.edu.EvaluationWeb.entity.Mark;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Subject;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Long> {

	List<Mark> findByStudentAndSubject(Profile student, Subject subject);

	@Query("select distinct m.subject from Mark as m where m.student = :student")
	List<Subject> getStudyingSubjects(Profile student);

}
