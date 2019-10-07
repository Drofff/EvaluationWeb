package com.edu.EvaluationWeb.dto;

public class TestResultDto {

    private String name;

    private Long numberOfQuestions;

    private Long grade;

    private String dateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Long numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
