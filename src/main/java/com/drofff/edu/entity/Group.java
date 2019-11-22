package com.drofff.edu.entity;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "group_info")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "groups")
    private List<Test> testList;

    @OneToMany(mappedBy = "group")
    private List<Profile> members;

    @ManyToMany
    @JoinTable(name = "group_teacher",
        joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private List<Profile> teachers;

    public Group() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Test> getTestList() {
        return testList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }


    public List<Profile> getMembers() {
        return members;
    }

    public void setMembers(List<Profile> members) {
        this.members = members;
    }

    public List<Profile> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Profile> teachers) {
        this.teachers = teachers;
    }

    @Override
    public int hashCode() {
    	return Objects.nonNull(id) ? id.intValue() : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Group) {
            Group group = (Group) obj;
            return this.id.equals(group.id);
        }
        return super.equals(obj);
    }
}
