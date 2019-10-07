package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.dto.TestDto;
import com.edu.EvaluationWeb.entity.*;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.exception.ValidationException;
import com.edu.EvaluationWeb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final UserContext userContext;
    private final TestResultRepository testResultRepository;
    private final ProfileRepository profileRepository;
    private final QuestionsRepository questionsRepository;
    private final GroupRepository groupRepository;
    private final ValidationService validationService;
    private final AnswerRepository answerRepository;

    private Map<User, List<Question>> questionsForUser = new HashMap<>();

    private Map<User, List<String>> userAnswers = new HashMap<>();

    private Map<User, LocalDateTime> startTime = new HashMap<>();

    @Autowired
    public TestService(TestRepository testRepository, UserContext userContext, TestResultRepository testResultRepository,
                       ProfileRepository profileRepository, QuestionsRepository questionsRepository,
                       GroupRepository groupRepository, ValidationService validationService, AnswerRepository answerRepository) {
        this.testRepository = testRepository;
        this.userContext = userContext;
        this.testResultRepository = testResultRepository;
        this.profileRepository = profileRepository;
        this.questionsRepository = questionsRepository;
        this.groupRepository = groupRepository;
        this.validationService = validationService;
        this.answerRepository = answerRepository;
    }

    public List<Test> getActiveTests(User user) {

        Profile profile = profileRepository.findByUserId(user);

        return testRepository.findAll().stream()
                .filter(Test::getActive)
                .filter( x -> x.getGroups().contains(profile.getGroup()) )
                .filter( x -> testResultRepository.findByTestAndStudent(x, profile) == null )
                .collect(Collectors.toList());

    }

    public boolean isUserOnTest(Profile profile) {
        return testResultRepository.findAll().stream().anyMatch(x -> x.getDateTime() == null && x.getStudent().getId().equals(profile.getId()));
    }

    public List<Question> getQuestions(User user, Test test) {

        if (questionsForUser.containsKey(user)) {

            return questionsForUser.get(user);

        } else {

            List<Question> allQuestions = questionsRepository.findByTest(test);

            List<Question> userQuestions = new LinkedList<>();

            Integer numberOfQuestions = test.getNumberOfQuestions();

            if (numberOfQuestions > allQuestions.size()) {

                throw new ArrayIndexOutOfBoundsException("Number of questions is greater than actual size of questions list");

            }

            Question question = null;

            for (int i = 0; i < numberOfQuestions; i++) {

                do {

                    Collections.shuffle(allQuestions);

                    question = allQuestions.get(i);

                } while (userQuestions.contains(question));

                userQuestions.add(question);

            }

            questionsForUser.put(user, userQuestions);

            return userQuestions;

        }

    }

    public void startTest(User user) {
        startTime.put(user, LocalDateTime.now());
    }

    public Integer checkTestTime(Integer minutes) {

        User user = userContext.getCurrentUser();

        if (startTime.containsKey(user)) {

            LocalDateTime time = startTime.get(user).plusMinutes(minutes);

            LocalDateTime now = LocalDateTime.now();

            if (time.isAfter(now) || time.isEqual(now)) return (int) Duration.between(now, time).toMinutes();

        }

        return -1;
    }

    public void clearTestData(User user) {

        if (questionsForUser.containsKey(user)) {

            questionsForUser.remove(user);

        }

        if (userAnswers.containsKey(user)) {

            userAnswers.remove(user);

        }

        if (startTime.containsKey(user)) {

            startTime.remove(user);

        }

    }

    //format question_number:answer_number

    public void answer(User user, String choice) {

        if (userAnswers.containsKey(user)) {

            List<String> answers = userAnswers.get(user);

            if (choice.split(":")[1].equals("-1")) {

                answers.remove(answers.stream()
                        .filter(x -> x.split(":")[0].equals(choice.split(":")[0]))
                        .findFirst().get());

            } else {

                Optional <String> answer = answers.stream()
                        .filter(x -> x.split(":")[0].equals(choice.split(":")[0]))
                        .findFirst();

                if (answer.isPresent()) {
                    answers.remove(answer.get());
                }

                answers.add(choice);

            }

        } else {

            List<String> answ = new LinkedList<>();

            answ.add(choice);

            userAnswers.put(user, answ);

        }

    }

    public List<String> getAnswers(User user) {
        if (userAnswers.containsKey(user)) {
            return userAnswers.get(user);
        }

        return new ArrayList<>();
    }

    public String getResultLevel(Integer grade, Integer from) {

        double percent = (float) grade / (float) from;

        if (percent <= 0.3) {
            return "Low";
        } else if (percent > 3 && percent <= 0.7) {
            return "Middle";
        } else {
            return "High";
        }

    }

    public Set<Group> parseGroupsByNames(Set<String> groupsNames) {
        return groupsNames.stream()
                .map(x -> {
                    Group group = groupRepository.findByName(x).orElse(null);
                    if(Objects.isNull(group)) {
                        throw new BaseException("Invalid group name provided");
                    }
                    return group;
                })
                .collect(Collectors.toSet());
    }

    public void save(Test test) {
        User currentUser = userContext.getCurrentUser();
        test.setTeacher(currentUser.getProfile());
        test.setNumberOfQuestions(test.getQuestions().size());
        validateTest(test);
        saveAnswers(test);
        saveQuestions(test);
        saveTest(test);
    }

    private void saveAnswers(Test test) {
        test.getQuestions().forEach(question -> {
            question.getAnswers().forEach(answer -> {
                Long id = saveIfTestOrAnswerOrQuestion(answer);
                answer.setId(id);
            });
        });
    }

    private void saveQuestions(Test test) {
        test.getQuestions().forEach(question -> {
            Long id = saveIfTestOrAnswerOrQuestion(question);
            question.setId(id);
        });
    }

    private void saveTest(Test test) {
        saveIfTestOrAnswerOrQuestion(test);
    }

    private <T> Long saveIfTestOrAnswerOrQuestion(T t) {
        if(Test.class.isAssignableFrom(t.getClass())) {
            return testRepository.save((Test) t).getId();
        } else if(Question.class.isAssignableFrom(t.getClass())) {
            return questionsRepository.save((Question) t).getId();
        } else if(Answer.class.isAssignableFrom(t.getClass())) {
            return answerRepository.save((Answer) t).getId();
        }
        return null;
    }

    public Test getById(Long id) {
        return testRepository.findById(id).orElse(null);
    }

    public void update(Test test) {
        if(Objects.isNull(test.getId())) {
            throw new BaseException("Can not update test caused by id absence");
        }
        Test testFromDb = testRepository.findById(test.getId()).orElse(null);
        if(Objects.isNull(testFromDb)) {
            throw new BaseException("Test with such id do not exists");
        }
        if(!userContext.isCurrentUser(testFromDb.getTeacher().getUserId())) {
            throw new BaseException("Invalid permissions");
        }
        save(test);
    }

    public void deleteById(Long id) {
        Test test = testRepository.findById(id).orElse(null);
        if(Objects.isNull(test)) {
            throw new BaseException("Test with such id do not exists");
        }
        validateTestPermissions(test);
        testRepository.deleteById(id);
    }

    private void validateTestPermissions(Test test) {
        Profile testTeacher = test.getTeacher();
        User currentUser = userContext.getCurrentUser();
        if(!testTeacher.getUserId().getId().equals(currentUser.getId())) {
            throw new BaseException("Invalid permissions");
        }
    }

    private void validateTest(Test test) {
        Map<String, String> testInfoErrors = validationService.validateRecursively(test);
        if(!testInfoErrors.isEmpty()) {
            throw new ValidationException("Invalid test data provided", testInfoErrors);
        }
    }

    public void activateTest(Long id) {
        Test testToActivate = testRepository.findById(id)
                .orElseThrow(() -> new BaseException("Invalid test id"));
        Profile testOwner = testToActivate.getTeacher();
        if(!userContext.isCurrentUser(testOwner.getUserId())) {
            throw new BaseException("Invalid permissions");
        }
        if(testToActivate.getDeadLine().isBefore(LocalDateTime.now())) {
            throw new BaseException("Change test deadline to activate it");
        }
        testToActivate.setActive(true);
        testRepository.save(testToActivate);
    }

}
