package com.drofff.edu.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.drofff.edu.dto.AnswerDto;
import com.drofff.edu.dto.QuestionDto;
import com.drofff.edu.entity.Answer;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Question;
import com.drofff.edu.entity.Test;

public class TestUtils {

	private static final String ADDITIONAL_FIRST_QUESTION_ANSWER_PATTERN = "^a([02-9]|\\d{2,})q1$";

	private TestUtils() {}

	public static List<AnswerDto> getAdditionalAnswersForFirstQuestionFromParams(Map<String, String[]> params) {
		return params.entrySet().stream()
				.filter(param -> ParseUtils.matches(param.getKey(), ADDITIONAL_FIRST_QUESTION_ANSWER_PATTERN))
				.map(param -> toAnswerDto(param, params))
				.collect(Collectors.toList());
	}

	private static AnswerDto toAnswerDto(Map.Entry<String, String[]> param, Map<String, String[]> params) {
		AnswerDto answerDto = new AnswerDto();
		answerDto.setName(param.getKey());
		answerDto.setAnswer(param.getValue()[0]);
		answerDto.setRight(isRightAnswer(answerDto.getName(), params));
		return answerDto;
	}

	private static boolean isRightAnswer(String answerName, Map<String, String[]> params) {
		String rightAnswerKey = answerName + "r";
		return params.containsKey(rightAnswerKey);
	}

	public static Set<QuestionDto> getQuestionDtoSet(Test test) {
		Set<QuestionDto> questionDtoSet = new HashSet<>();
		int questionIndex = 1;
		for(Question question : test.getQuestions()) {
			question.setParameterName("q" + questionIndex++);
			List<AnswerDto> answerDtoList = getAnswerDtoList(question);
			QuestionDto questionDto = toQuestionDto(question, answerDtoList);
			questionDtoSet.add(questionDto);
		}
		return questionDtoSet;
	}

	private static List<AnswerDto> getAnswerDtoList(Question question) {
		List<Long> rightAnswers = ParseUtils.parseAnswersString(question.getRightAnswer());
		List<AnswerDto> answerDtoList = new ArrayList<>();
		IntStream.range(0, question.getAnswers().size())
				.forEach(index -> {
					Answer answer = question.getAnswers().get(index);
					String name = "a" + (index + 1) + question.getParameterName();
					boolean isRight = rightAnswers.contains(answer.getId());
					answerDtoList.add(new AnswerDto(name, answer.getText(), isRight));
				});
		return answerDtoList;
	}

	private static QuestionDto toQuestionDto(Question question, List<AnswerDto> answerDtoList) {
		QuestionDto questionDto = new QuestionDto(question);
		questionDto.setAnswers(answerDtoList);
		return questionDto;
	}

	public static List<String> toGroupNamesList(Set<Group> groups) {
		return groups.stream()
				.map(Group::getName)
				.collect(Collectors.toList());
	}

	public static Map<String, String> toSingleValueParamsMap(Map<String, String[]> params) {
		return params.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, param -> param.getValue()[0]));
	}

}
