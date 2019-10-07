package com.edu.EvaluationWeb.dto;

import java.util.List;

public class TeacherProfileDto extends ProfileDto {

    public TeacherProfileDto() {}

    public TeacherProfileDto(ProfileDto profileDto) {
        super(profileDto.getFirstName(), profileDto.getLastName(),
                profileDto.getPosition(), profileDto.getPhotoUrl(),
                profileDto.getGroupName(), profileDto.getEmail());
    }

    private List<String> subjects;

    private List<String> studentsGroups;

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<String> getStudentsGroups() {
        return studentsGroups;
    }

    public void setStudentsGroups(List<String> studentsGroups) {
        this.studentsGroups = studentsGroups;
    }
}
