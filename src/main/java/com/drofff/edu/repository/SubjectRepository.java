package com.drofff.edu.repository;

import com.drofff.edu.entity.Subject;
import com.drofff.edu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByTeacher(User teacher);

}
