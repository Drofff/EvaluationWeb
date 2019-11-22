package com.drofff.edu.repository;

import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("from Test t where t.active = false and :dateTime >= t.startTime and :dateTime < t.deadLine")
    List<Test> findNotActiveBetweenStartTimeAndDeadLine(LocalDateTime dateTime);

    List<Test> findByTeacher(Profile teacher);
}
