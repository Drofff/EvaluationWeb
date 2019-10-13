package com.edu.EvaluationWeb.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateUserDto {

    @NotNull(message = "Please, provide user mail")
    @Email(message = "Email format is invalid")
    private String email;

    @NotNull(message = "Please, provide user group")
    private Long groupId;

    @NotBlank(message = "Please, provide user position")
    private String position;

    private Boolean isTeacher;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
    }
}
