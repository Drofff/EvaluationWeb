package com.drofff.edu.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    @NotNull
    private Set<Question> questions;

    private Integer numberOfQuestions;

    @NotNull
    @Max(value = 999)
    @Min(value = 1)
    private Integer duration;

    private Boolean active;

    @NotNull
    private LocalDateTime deadLine;

    private LocalDateTime startTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Profile teacher;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "open_test",
            joinColumns = { @JoinColumn(name = "test_id") },
            inverseJoinColumns = { @JoinColumn(name = "group_id") }
            )
    private Set<Group> groups;

    public Test() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDateTime deadLine) {
        this.deadLine = deadLine;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Profile getTeacher() {
        return teacher;
    }

    public void setTeacher(Profile teacher) {
        this.teacher = teacher;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
