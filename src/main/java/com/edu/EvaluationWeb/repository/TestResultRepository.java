package com.edu.EvaluationWeb.repository;

import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Test;
import com.edu.EvaluationWeb.entity.TestResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends PagingAndSortingRepository<TestResult, Long> {

    List<TestResult> findAll();

    @Query(value = "select * from result_of_tests where test_id = :test and student_id = :student", nativeQuery = true)
    TestResult findByTestAndStudent(Test test, Profile student);

    @Query(value = "select * from result_of_tests where student_id = :profile and date_time is null limit 1", nativeQuery = true)
    TestResult findActiveTest(Profile profile);

    List<TestResult> findByStudent(Profile profile, Sort sort);

}
