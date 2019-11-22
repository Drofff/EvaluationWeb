package com.drofff.edu.dto;

public class ProfileDto {

    private String firstName;

    private String lastName;

    private String position;

    private String photoUrl;

    private String groupName;

    private String email;

    public ProfileDto() {

    }

    public ProfileDto(String firstName, String lastName,
                      String position, String photoUrl,
                      String groupName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.photoUrl = photoUrl;
        this.groupName = groupName;
        this.email = email;
    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
