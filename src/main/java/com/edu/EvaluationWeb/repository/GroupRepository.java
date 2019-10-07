package com.edu.EvaluationWeb.repository;

import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("from Group g join g.teachers t where :teacher in t")
    Set<Group> findByTeacher(Profile teacher);

    Optional<Group> findByName(String name);

}
