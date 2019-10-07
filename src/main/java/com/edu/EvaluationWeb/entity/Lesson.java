package com.edu.EvaluationWeb.entity;

import com.edu.EvaluationWeb.component.validation.LimitedDuration;
import com.edu.EvaluationWeb.component.validation.ReachableDate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
public class Lesson implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Length(min = 2, max = 45, message = "Length of title must be between 2 and 45 symbols")
    private String title;

    private String homeTask;

    private String description;

    @Length(min = 1, max = 30, message = "Length of room must be between 1 and 30")
    private String room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Profile teacher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group groupId;

    @ReachableDate(message = "Please, provide valid date")
    private LocalDateTime dateTime;

    @LimitedDuration(message = "Maximum duration is 160 minutes", maxMinutes = 160)
    private Duration duration;

    private Boolean isTest;

    public Lesson() {}

    @Override
    public Lesson clone() {
        try {
            return (Lesson) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHomeTask() {
        return homeTask;
    }

    public void setHomeTask(String homeTask) {
        this.homeTask = homeTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Profile getTeacher() {
        return teacher;
    }

    public void setTeacher(Profile teacher) {
        this.teacher = teacher;
    }

    public Group getGroupId() {
        return groupId;
    }

    public void setGroupId(Group groupId) {
        this.groupId = groupId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getTest() {
        return isTest;
    }

    public void setTest(Boolean test) {
        isTest = test;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
