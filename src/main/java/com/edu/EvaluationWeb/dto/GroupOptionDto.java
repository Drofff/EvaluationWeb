package com.edu.EvaluationWeb.dto;

import com.edu.EvaluationWeb.entity.Group;

public class GroupOptionDto {

    private Long id;

    private String name;

    private boolean selected;

    public GroupOptionDto() {}

    public GroupOptionDto(Group group) {
        this.name = group.getName();
        this.id = group.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
