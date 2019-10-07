package com.edu.EvaluationWeb.mapper;

import com.edu.EvaluationWeb.dto.GroupDto;
import com.edu.EvaluationWeb.entity.Group;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper extends Mapper {

    public GroupDto toDto(Group group) {
        return super.getModelMapper().map(group, GroupDto.class);
    }

}
