package com.drofff.edu.mapper;

import com.drofff.edu.dto.TestDto;
import com.drofff.edu.entity.Test;
import com.drofff.edu.utils.ParseUtils;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
