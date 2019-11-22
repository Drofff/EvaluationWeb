package com.drofff.edu.dto;

import java.util.HashSet;
import java.util.Set;

public class TestDto {

    private String name;

    private Integer duration;

    private String deadLine;

    private String startTime;

    private Boolean active;

    private Set<String> groups = new HashSet<>();

    public TestDto() {}

    public TestDto(TestDto testDto) {
    	name = testDto.name;
    	duration = testDto.duration;
    	startTime = testDto.startTime;
    	deadLine = testDto.deadLine;
    	active = testDto.active;
    	groups = testDto.groups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public void setGroups(Set<String> groups) {
        this.groups = groups;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
