package com.drofff.edu.dto;

public class ManageTestDto extends TestDto {

	private Long id;

	private Integer numberOfQuestions;

	public ManageTestDto(TestDto testDto) {
		super(testDto);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumberOfQuestions() {
		return numberOfQuestions;
	}

	public void setNumberOfQuestions(Integer numberOfQuestions) {
		this.numberOfQuestions = numberOfQuestions;
	}

}
