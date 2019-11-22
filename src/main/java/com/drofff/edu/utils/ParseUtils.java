package com.drofff.edu.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.drofff.edu.repository.AnswerRepository;
import com.drofff.edu.entity.Answer;
import com.drofff.edu.entity.Question;
import com.drofff.edu.exception.BaseException;

@Component
public class ParseUtils {

    private static final String QUESTION_NAME_PATTERN = "^q(\\d+)$";
    private static final String ANSWER_NAME_PATTERN = "^a(\\d+)q(\\d+)$";
    private static final String QUESTION_INDEX_PREFIX = "q";
    private static final String RIGHT_ANSWER_NAME_PATTERN = "^a(\\d+)q(\\d+)r$";
    private static final String LOCAL_DATE_TIME_FORMAT_PATTERN = "uuuu-MM-dd'T'HH:mm";
	private static final String RIGHT_ANSWERS_SEPARATOR = ",";

    private final AnswerRepository answerRepository;

    @Autowired
    private ParseUtils(AnswerRepository answerRepository) {
	    this.answerRepository = answerRepository;
    }

    public static LocalDateTime parseDeadlineFromString(String dateTime) {
        try {
            return parseLocalDateTimeFromString(dateTime);
        } catch(DateTimeParseException e) {
            throw new BaseException("Invalid deadline value format");
        }
    }

	private static List<String> parseRightAnswers(Map<String, String[]> parameters) {
		return parameters.keySet().stream()
				.filter(param -> matches(param, RIGHT_ANSWER_NAME_PATTERN))
				.collect(Collectors.toList());
	}

	private static Question parseQuestionFromParametersMapEntry(Map.Entry<String, String[]> entry) {
		String [] value = entry.getValue();
		if(Objects.nonNull(value) && value.length == 1) {
			return wrapQuestion(value[0], entry.getKey());
		}
		return null;
	}

	private static Map<String, Answer> parseAnswersWithQuestionIndex(Integer questionIndex, Map<String, String[]> parameters) {
		Map<String, String> answers = getAnswersFromParameters(parameters);
		Map<String, String> answersWithQuestionIndex = filterAnswersByQuestionIndex(questionIndex, answers);
		return wrapAnswers(answersWithQuestionIndex);
	}

	private static Map<String, String> getAnswersFromParameters(Map<String, String[]> parameters) {
		return parameters.entrySet().stream()
				.filter(param -> matches(param.getKey(), ANSWER_NAME_PATTERN))
				.collect(Collectors.toMap(Map.Entry::getKey, param -> getAnswerValue(param.getValue())));
	}

	private static String getAnswerValue(String [] value) {
		if(Objects.nonNull(value) && value.length > 0) {
			return value[0];
		}
		throw new BaseException("Invalid answer provided");
	}

	private static Map<String, String> filterAnswersByQuestionIndex(Integer questionIndex, Map<String, String> answers) {
		String questionIndexSuffix = QUESTION_INDEX_PREFIX + questionIndex;
		return answers.entrySet().stream()
				.filter(answer -> answer.getKey().endsWith(questionIndexSuffix))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private static Integer extractQuestionIndexFromName(String name) {
		Matcher matcher = Pattern.compile(QUESTION_NAME_PATTERN).matcher(name);
		if(matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		return null;
	}

	private static Map<String, Answer> wrapAnswers(Map<String, String> answers) {
		return answers.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, param -> wrapAnswer(param.getValue())));
	}

	private static Answer wrapAnswer(String answerText) {
		Answer answer = new Answer();
		answer.setText(answerText);
		return answer;
	}

	private static Question wrapQuestion(String questionText) {
		Question question = new Question();
		question.setQuestion(questionText);
		return question;
	}

	private static Question wrapQuestion(String questionText, String parameterName) {
		Question question = wrapQuestion(questionText);
		question.setParameterName(parameterName);
		return question;
	}

	public static boolean matches(String name, String pattern) {
		return Pattern.compile(pattern).matcher(name).find();
	}

    public static LocalDateTime parseStartTimeFromString(String startTime) {
        try {
            return parseLocalDateTimeFromString(startTime);
        } catch(DateTimeParseException e) {
            throw new BaseException("Invalid start time value format");
        }
    }

    private static LocalDateTime parseLocalDateTimeFromString(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT_PATTERN);
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }

	private static List<Question> parseQuestions(Map<String, String[]> parameters) {
		return parameters.entrySet().stream()
				.filter(param -> matches(param.getKey(), QUESTION_NAME_PATTERN))
				.map(ParseUtils::parseQuestionFromParametersMapEntry)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}

	private static String buildRightAnswerString(List<String> rightAnswers, Map<String, Answer> answers) {
		List<String> rightAnswersNames = getAnswersNamesFromRightAnswers(rightAnswers);
		return answers.entrySet().stream()
				.filter(answer -> rightAnswersNames.contains(answer.getKey()))
				.map(answer -> answer.getValue().getId().toString())
				.collect(Collectors.joining(RIGHT_ANSWERS_SEPARATOR));
	}

	private static List<String> getAnswersNamesFromRightAnswers(List<String> rightAnswers) {
		return rightAnswers.stream()
				.map(rightAnswer -> rightAnswer.replace("r", ""))
				.collect(Collectors.toList());
	}


	public List<Question> parseQuestionsWithAnswersFromParametersMap(Map<String, String[]> parameters) {
		List<Question> questions = parseQuestions(parameters);
		questions.forEach(question -> setAnswersIntoQuestionFromParametersMap(question, parameters));
		return questions;
	}

    private void setAnswersIntoQuestionFromParametersMap(Question question, Map<String, String[]> parameters) {
        Integer questionIndex = extractQuestionIndexFromName(question.getParameterName());
        Map<String, Answer> answers = parseAnswersWithQuestionIndex(questionIndex, parameters);
        Map<String, Answer> savedAnswers = saveAnswers(answers);
	    question.setAnswers(new ArrayList<>(savedAnswers.values()));
	    List<String> rightAnswers = parseRightAnswers(parameters);
        String rightAnswer = buildRightAnswerString(rightAnswers, savedAnswers);
        question.setRightAnswer(rightAnswer);
    }

    private Map<String, Answer> saveAnswers(Map<String, Answer> answers) {
    	return answers.entrySet().stream()
			    .collect(Collectors.toMap(Map.Entry::getKey, answer -> saveAnswer(answer.getValue())));
    }

    private Answer saveAnswer(Answer answer) {
    	return answerRepository.save(answer);
    }

    public static List<Long> parseAnswersString(String answersStr) {
    	String [] answers = answersStr.split(RIGHT_ANSWERS_SEPARATOR);
	    return Arrays.stream(answers)
			    .map(ParseUtils::parseAnswerString)
			    .filter(Optional::isPresent)
			    .map(Optional::get)
			    .collect(Collectors.toList());
    }

    private static Optional<Long> parseAnswerString(String answerStr) {
    	try {
    		Long answerId = Long.parseLong(answerStr);
    		return Optional.of(answerId);
	    } catch(NumberFormatException e) {
    		return Optional.empty();
	    }
    }

}
