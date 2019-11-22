package com.drofff.edu.mapper;

import com.drofff.edu.dto.LessonDto;
import com.drofff.edu.entity.Lesson;
import com.drofff.edu.entity.Profile;

import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class LessonMapper extends Mapper {

    private TypeMap<Lesson, LessonDto> toDtoTypeMap;

    public LessonMapper() {
        toDtoTypeMap = super.getModelMapper().createTypeMap(Lesson.class, LessonDto.class)
                                .addMappings(x -> x.using(y -> ((LocalDateTime)y.getSource())
                                                            .toLocalTime()
                                                            .truncatedTo(ChronoUnit.MINUTES).toString())
                                                    .map(Lesson::getDateTime, LessonDto::setTime))
                                .addMappings(x -> x.using(y -> ((Duration)y.getSource()).toMinutes())
                                                    .map(Lesson::getDuration, LessonDto::setDuration))
                                .addMappings(x -> x.using(y -> ((Profile)y.getSource()).getFirstName() + " " +
                                        ((Profile)y.getSource()).getLastName())
                                                .map(Lesson::getTeacher, LessonDto::setTeacherFullName));
    }

    public LessonDto toDto(Lesson entity) {
        return toDtoTypeMap.map(entity);
    }

}
