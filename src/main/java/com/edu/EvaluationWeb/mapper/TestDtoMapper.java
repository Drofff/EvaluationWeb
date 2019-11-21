package com.edu.EvaluationWeb.mapper;

import com.edu.EvaluationWeb.dto.TestDto;
import com.edu.EvaluationWeb.entity.Question;
import com.edu.EvaluationWeb.entity.Test;
import com.edu.EvaluationWeb.utils.ParseUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class TestDtoMapper extends Mapper {

    public Test toEntity(TestDto testDto) {
        Test test = super.getModelMapper().map(testDto, Test.class);
	    if(Objects.isNull(testDto.getActive())) {
		    test.setActive(false);
	    }
	    if(Objects.nonNull(testDto.getDeadLine())) {
		    LocalDateTime deadline = ParseUtils.parseDeadlineFromString(testDto.getDeadLine());
		    test.setDeadLine(deadline);
	    }
	    if(Objects.nonNull(testDto.getStartTime())) {
		    LocalDateTime startTime = ParseUtils.parseStartTimeFromString(testDto.getStartTime());
		    test.setStartTime(startTime);
	    }
	    return test;
    }

    public TestDto toDto(Test test) {
    	return super.getModelMapper().map(test, TestDto.class);
    }

}
