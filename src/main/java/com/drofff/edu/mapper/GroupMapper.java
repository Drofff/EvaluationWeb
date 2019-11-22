package com.drofff.edu.mapper;

import com.drofff.edu.dto.GroupDto;
import com.drofff.edu.entity.Group;

import org.springframework.stereotype.Component;

@Component
public class GroupMapper extends Mapper {

    public GroupDto toDto(Group group) {
        return super.getModelMapper().map(group, GroupDto.class);
    }

}
