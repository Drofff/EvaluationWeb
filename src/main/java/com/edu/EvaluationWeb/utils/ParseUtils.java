package com.edu.EvaluationWeb.utils;

import com.edu.EvaluationWeb.entity.Answer;
import com.edu.EvaluationWeb.entity.Question;
import com.edu.EvaluationWeb.exception.BaseException;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ParseUtils {

    private static final String QUESTION_NAME_PATTERN = "^q(\\d+)$";
    private static final String ANSWER_NAME_PREFIX = "aq";
    private static final String RIGHT_ANSWER_NAME_PREFIX = ANSWER_NAME_PREFIX;
    private static final String RIGHT_ANSWER_NAME_SUFFIX = "r";
    private static final String LOCAL_DATE_TIME_FORMAT_PATTERN = "uuuu-MM-dd'T'HH:mm";

    private ParseUtils() {}

    public static LocalDateTime parseDeadlineFromString(String dateTime) {
        try {
            return parseLocalDateTimeFromString(dateTime);
        } catch(DateTimeParseException e) {
            throw new BaseException("Invalid deadline value format");
        }
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

    public static List<Question> parseQuestionsWithAnswersFromParametersMap(Map<String, String[]> parameters) {
        List<Question> questions = parseQuestions(parameters);
        questions.forEach(question -> setAnswersIntoQuestionFromParametersMap(question, parameters));
        return questions;
    }

    private static void setAnswersIntoQuestionFromParametersMap(Question question, Map<String, String[]> parameters) {
        Integer questionIndex = extractQuestionIndexFromName(question.getParameterName());
        List<Answer> answers = parseAnswersWithQuestionIndex(questionIndex, parameters);
        question.setAnswers(answers);
        String rightAnswer = parseRightAnswerByQuestionIndex(questionIndex, parameters);
        question.setRightAnswer(rightAnswer);
    }

    private static List<Question> parseQuestions(Map<String, String[]> parameters) {
        return parameters.entrySet().stream()
                .filter(param -> matches(param.getKey(), QUESTION_NAME_PATTERN))
                .map(ParseUtils::parseQuestionFromParametersMapEntry)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static Question parseQuestionFromParametersMapEntry(Map.Entry<String, String[]> entry) {
        String [] value = entry.getValue();
        if(Objects.nonNull(value) && value.length == 1) {
            return wrapQuestion(value[0], entry.getKey());
        }
        return null;
    }

    private static String parseRightAnswerByQuestionIndex(Integer questionIndex, Map<String, String[]> parameters) {
        String rightAnswerName = RIGHT_ANSWER_NAME_PREFIX + questionIndex + RIGHT_ANSWER_NAME_SUFFIX;
        if(parameters.containsKey(rightAnswerName)) {
            String [] rightAnswer = parameters.get(rightAnswerName);
            if(Objects.nonNull(rightAnswer) && rightAnswer.length == 1) {
                return rightAnswer[0];
            }
        }
        return null;
    }

    private static List<Answer> parseAnswersWithQuestionIndex(Integer questionIndex, Map<String, String[]> parameters) {
        String answerName = ANSWER_NAME_PREFIX + questionIndex;
        if(!parameters.containsKey(answerName)) {
            return Collections.emptyList();
        }
        String [] answerValue = parameters.get(answerName);
        if(Objects.isNull(answerValue) || answerValue.length == 0) {
            return Collections.emptyList();
        }
        return wrapAnswers(answerValue);
    }

    private static Integer extractQuestionIndexFromName(String name) {
        Matcher matcher = Pattern.compile(QUESTION_NAME_PATTERN).matcher(name);
        if(matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    private static List<Answer> wrapAnswers(String [] answers) {
        return Arrays.stream(answers)
                .map(ParseUtils::wrapAnswer)
                .collect(Collectors.toList());
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

    private static boolean matches(String name, String pattern) {
        return Pattern.compile(pattern).matcher(name).find();
    }

}
