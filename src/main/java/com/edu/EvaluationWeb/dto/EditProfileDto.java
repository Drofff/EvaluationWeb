package com.edu.EvaluationWeb.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;

public class EditProfileDto {

    @Pattern(regexp = "[A-Za-z]+", message = "First name can not be empty and should contain only letters")
    private String firstName;

    @Pattern(regexp = "[A-Za-z]+", message = "Last name can not be empty and should contain only letters")
    private String lastName;

    public EditProfileDto() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
