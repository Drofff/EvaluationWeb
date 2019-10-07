package com.edu.EvaluationWeb.repository;

import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Profile findByUserId(User user);

    List<Profile> findByGroup(Group group);

    @Query("select p from Profile p join p.group g join g.teachers t where :teacher in t")
    Page<Profile> findByTeacher(Profile teacher, Pageable pageable);

    @Query("select p from Profile p join p.group g where g in :groups and CONCAT(p.firstName,' ', p.lastName) like CONCAT(:name, '%')")
    Page<Profile> findByNameAndInGroups(String name, Set<Group> groups, Pageable pageable);

    @Query("select p from Profile p where CONCAT(p.firstName,' ', p.lastName) like CONCAT(:name, '%')")
    Page<Profile> findByName(String name, Pageable pageable);

    @Query("select p from Profile p join p.group g where g in :groups")
    Page<Profile> findByInGroups(Set<Group> groups, Pageable pageable);

}
