package com.drofff.edu.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import com.drofff.edu.entity.Mark;
import com.drofff.edu.dto.MarkDto;

@Component
public class MarkDtoMapper extends Mapper {

	private static final String DATE_TIME_FORMAT_PATTERN = "dd LLLL uuuu' at 'hh':'mm a";

	private final TypeMap<Mark, MarkDto> toDtoTypeMap;

	public MarkDtoMapper() {
		toDtoTypeMap = super.getModelMapper()
				.createTypeMap(Mark.class, MarkDto.class)
				.addMappings(x -> x.using(getLocalDateTimeConverter())
						.map(Mark::getLocalDateTime, MarkDto::setLocalDateTime));
	}

	private Converter<LocalDateTime, String> getLocalDateTimeConverter() {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN);
		return x -> x.getSource().format(dateTimeFormatter);
	}

	public MarkDto toDto(Mark mark) {
		return toDtoTypeMap.map(mark);
	}

}
