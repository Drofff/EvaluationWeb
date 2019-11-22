package com.drofff.edu.mapper;

import com.drofff.edu.dto.EditLessonDto;
import com.drofff.edu.entity.Lesson;
import com.drofff.edu.repository.GroupRepository;

import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class EditLessonDtoMapper extends Mapper {

    private final TypeMap<EditLessonDto, Lesson> toEntityTypeMap;
    private final GroupRepository groupRepository;

    @Autowired
    public EditLessonDtoMapper(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        this.toEntityTypeMap = super.getModelMapper().createTypeMap(EditLessonDto.class, Lesson.class)
                .addMappings(x -> x.using(y -> groupRepository.findById((Long)y.getSource()).get())
                        .map(EditLessonDto::getGroupId, Lesson::setGroupId))
                .addMappings(x -> x.using(y -> LocalDateTime.parse((String) y.getSource()))
                        .map(EditLessonDto::getDateTime, Lesson::setDateTime))
                .addMappings(x -> x.using(y -> Duration.of((Integer)y.getSource(), ChronoUnit.MINUTES))
                        .map(EditLessonDto::getDuration, Lesson::setDuration));
    }

    public Lesson toEntity(EditLessonDto editLessonDto) {
        return this.toEntityTypeMap.map(editLessonDto);
    }

}
