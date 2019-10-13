package com.edu.EvaluationWeb.dto;

import com.edu.EvaluationWeb.constants.ProfileConstants;

import javax.validation.constraints.Pattern;

public class EditProfileDto {

    @Pattern(regexp = ProfileConstants.NAME_REGEX, message = "First name can not be empty and should contain only letters")
    private String firstName;

    @Pattern(regexp = ProfileConstants.NAME_REGEX, message = "Last name can not be empty and should contain only letters")
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
