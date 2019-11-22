package com.drofff.edu.dto;

public class AnswerDto {

    private String name;
    private String answer;
    private boolean isRight;

    public AnswerDto() {}

    public AnswerDto(String name, String answer, boolean isRight) {
    	this.name = name;
    	this.answer = answer;
    	this.isRight = isRight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
