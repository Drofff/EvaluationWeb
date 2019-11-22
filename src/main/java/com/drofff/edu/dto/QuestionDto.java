package com.drofff.edu.dto;

import java.util.List;

import com.drofff.edu.entity.Question;

public class QuestionDto {

	private Long id;

	private String question;

	private List<AnswerDto> answers;

	private String parameterName;

	public QuestionDto(Question question) {
		this.id = question.getId();
		this.question = question.getQuestion();
		this.parameterName = question.getParameterName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<AnswerDto> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDto> answers) {
		this.answers = answers;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
}
