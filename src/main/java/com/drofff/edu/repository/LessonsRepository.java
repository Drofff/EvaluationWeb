package com.drofff.edu.repository;

import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Lesson;
import com.drofff.edu.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LessonsRepository extends JpaRepository<Lesson, Long> {

    @Query(value = "select * from lesson where date_time >= :startDate AND date_time < :endDate", nativeQuery = true)
    List<Lesson> findByDate(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(*) from Lesson l where l.dateTime >= :startDate and l.dateTime <= :endDate and ( l.groupId = :group or l.teacher = :teacher )")
    Integer countByDate(LocalDateTime startDate, LocalDateTime endDate, Group group, Profile teacher);

    @Query(value = "select * from lesson where date_time >= :startDate AND date_time < :endDate", nativeQuery = true)
    List<Lesson> findByDate(LocalDate startDate, LocalDate endDate);

    List<Lesson> findByDateTimeBetweenAndIsTest(LocalDateTime startDate, LocalDateTime endDate, Boolean test);

    @Query(value = "select * from lesson where date_time <= :dateTime and ( group_id = :group or teacher_id = :teacher ) order by date_time DESC limit 1", nativeQuery = true)
    Lesson findFirstLeftNeighbour(LocalDateTime dateTime, Long group, Long teacher);

    @Query(value = "select * from lesson where date_time like CONCAT(:date, '%')", nativeQuery = true)
    List<Lesson> findByDate(String date);

}
