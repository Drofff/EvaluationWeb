package com.drofff.edu.dto;

public class ProfileAdminDto extends ProfileDto {

    private Boolean isTeacher;

    private Long userId;

    private Long profileId;

    public ProfileAdminDto() {}

    public ProfileAdminDto(ProfileDto profileDto) {
        super(profileDto.getFirstName(), profileDto.getLastName(),
                profileDto.getPosition(), profileDto.getPhotoUrl(),
                profileDto.getGroupName(), profileDto.getEmail());
    }

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }
}
