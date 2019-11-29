package com.drofff.edu.dto;

public class StudentDto extends ProfileDto {

	private Long id;

	private String status;

	public StudentDto() {}

	public StudentDto(ProfileDto profileDto) {
		super(profileDto.getFirstName(), profileDto.getLastName(),
				profileDto.getPosition(), profileDto.getPhotoUrl(),
				profileDto.getGroupName(), profileDto.getEmail());
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
