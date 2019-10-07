package com.edu.EvaluationWeb.repository;

import com.edu.EvaluationWeb.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("from Test t where not t.active and :dateTime >= t.startTime and :dateTime < t.deadLine")
    List<Test> findNotActiveBetweenStartTimeAndDeadLine(LocalDateTime dateTime);
}
