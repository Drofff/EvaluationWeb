package com.drofff.edu.repository;

import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.Test;
import com.drofff.edu.entity.TestResult;
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
