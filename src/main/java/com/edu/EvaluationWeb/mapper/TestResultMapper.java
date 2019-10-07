package com.edu.EvaluationWeb.mapper;

import com.edu.EvaluationWeb.dto.TestResultDto;
import com.edu.EvaluationWeb.entity.TestResult;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TestResultMapper extends Mapper {

    private final TypeMap<TestResult, TestResultDto> toDtoTypeMap;

    public TestResultMapper() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd' 'HH:mm");
        this.toDtoTypeMap = super.getModelMapper()
                .createTypeMap(TestResult.class, TestResultDto.class)
                .addMapping(x -> x.getTest().getName(), TestResultDto::setName)
                .addMapping(x -> x.getTest().getNumberOfQuestions(), TestResultDto::setNumberOfQuestions)
                .addMappings(x -> x.when(y -> y.getSource() != null)
                                .using(y -> ((LocalDateTime)y.getSource()).format(dateTimeFormatter))
                                .map(TestResult::getDateTime, TestResultDto::setDateTime));
    }


    public TestResultDto toDto(TestResult testResult) {
        return toDtoTypeMap.map(testResult);
    }

}
