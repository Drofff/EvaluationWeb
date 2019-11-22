package com.drofff.edu.mapper;

import com.drofff.edu.dto.CreateLessonDto;
import com.drofff.edu.entity.Lesson;
import com.drofff.edu.repository.GroupRepository;

import org.modelmapper.TypeMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class CreateLessonDtoMapper extends Mapper implements InitializingBean {

    private final GroupRepository groupRepository;

    private TypeMap<CreateLessonDto, Lesson> toEntityTypeMap;

    @Autowired
    public CreateLessonDtoMapper(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Lesson toEntity(CreateLessonDto dto) {
        return toEntityTypeMap.map(dto);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.toEntityTypeMap = super.getModelMapper()
                .createTypeMap(CreateLessonDto.class, Lesson.class)
                    .addMappings(x -> x.using(y -> groupRepository.findById((Long)y.getSource()).get())
                                        .map(CreateLessonDto::getGroupId, Lesson::setGroupId))
                    .addMappings(x -> x.using(y -> Duration.of((Integer)y.getSource(), ChronoUnit.MINUTES))
                                        .map(CreateLessonDto::getDuration, Lesson::setDuration));
    }

}
