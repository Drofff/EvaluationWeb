package com.edu.EvaluationWeb.mapper;

import com.edu.EvaluationWeb.dto.TestDto;
import com.edu.EvaluationWeb.entity.Question;
import com.edu.EvaluationWeb.entity.Test;
import com.edu.EvaluationWeb.utils.ParseUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TestDtoMapper extends Mapper {

    public Test toEntity(TestDto testDto) {
        return super.getModelMapper().map(testDto, Test.class);
    }

}
