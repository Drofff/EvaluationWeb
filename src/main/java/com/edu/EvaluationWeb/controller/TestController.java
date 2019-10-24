package com.edu.EvaluationWeb.controller;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.dto.TestDto;
import com.edu.EvaluationWeb.dto.TestResultDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Question;
import com.edu.EvaluationWeb.entity.Test;
import com.edu.EvaluationWeb.entity.TestResult;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.exception.ValidationException;
import com.edu.EvaluationWeb.mapper.TestDtoMapper;
import com.edu.EvaluationWeb.mapper.TestResultMapper;
import com.edu.EvaluationWeb.repository.AnswerRepository;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import com.edu.EvaluationWeb.repository.TestRepository;
import com.edu.EvaluationWeb.repository.TestResultRepository;
import com.edu.EvaluationWeb.service.TestService;
import com.edu.EvaluationWeb.service.ValidationService;
import com.edu.EvaluationWeb.utils.ParseUtils;

@Controller
@RequestMapping("/test")
public class TestController {

    private final ProfileRepository profileRepository;
    private final TestService testService;
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final AnswerRepository answerRepository;
    private final ValidationService validationService;
    private final TestResultMapper testResultMapper;
    private final ParseUtils parseUtils;
    private final TestDtoMapper testDtoMapper;
    private final UserContext userContext;
    private final GroupRepository groupRepository;

    @Autowired
    public TestController(ProfileRepository profileRepository, TestService testService,
                          TestRepository testRepository, TestResultRepository testResultRepository,
                          AnswerRepository answerRepository, ValidationService validationService,
                          TestResultMapper testResultMapper, ParseUtils parseUtils,
                          TestDtoMapper testDtoMapper, UserContext userContext, GroupRepository groupRepository) {
        this.profileRepository = profileRepository;
        this.testService = testService;

        this.testRepository = testRepository;
        this.testResultRepository = testResultRepository;
        this.answerRepository = answerRepository;
        this.validationService = validationService;
        this.testResultMapper = testResultMapper;
        this.parseUtils = parseUtils;
        this.testDtoMapper = testDtoMapper;
        this.userContext = userContext;
        this.groupRepository = groupRepository;
    }

    @GetMapping
    public String getTestMainPage(Model model, @RequestParam(name = "error_message", required = false) String errorMessage) {

        if(Objects.nonNull(errorMessage)) {
            model.addAttribute("error_message", errorMessage);
        }

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();

        Map<Test, String> tests = new LinkedHashMap<>();

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd' 'HH:mm");

        testService.getActiveTests(user).stream()
                .filter(x -> x.getDeadLine().isAfter(LocalDateTime.now()) || x.getDeadLine().isEqual(LocalDateTime.now()))
                .sorted( (x, y) -> (int) ( x.getDeadLine().toEpochSecond(zonedDateTime.getOffset()) - y.getDeadLine().toEpochSecond(zonedDateTime.getOffset()) ) )
                .forEach(x -> tests.put(x, x.getDeadLine().format(dateTimeFormatter)));

        model.addAttribute("tests", tests);

        if (testService.isUserOnTest(profile)) {

            TestResult tr = testResultRepository.findActiveTest(profile);

            Test thisTest = tr.getTest();

            Integer timeLeft = testService.checkTestTime(thisTest.getDuration());

            if (timeLeft > 0) {
                model.addAttribute("currentTest", thisTest);
                model.addAttribute("timeLeft", timeLeft);
            } else {
                testService.clearTestData(user);

                tr.setDateTime(LocalDateTime.now());
                tr.setGrade(0l);

                testResultRepository.save(tr);
            }
        }

        return "myTestsPage";
    }

    @RequestMapping("/{testId}")
    public String startTestPage(@PathVariable Long testId, Model model) {

        try {

            User user = userContext.getCurrentUser();
            Profile profile = user.getProfile();

            Optional<Test> testOptional = testRepository.findById(testId)
                    .filter(Test::getActive);

            if(!testOptional.isPresent()) {
                throw new BaseException("Test with such id do not exists");
            }

            Test test = testOptional.get();

            TestResult currentTest = testResultRepository.findByTestAndStudent(test, profileRepository.findByUserId(user));

            if (currentTest != null && currentTest.getDateTime() == null) {

                return "redirect:/test/currentTest";

            } else if (testService.isUserOnTest(profile)) {
                model.addAttribute("otherTest", true);
            }

            model.addAttribute("test", test);
            model.addAttribute("teacher", test.getTeacher());
            model.addAttribute("deadline", test.getDeadLine().format(DateTimeFormatter.ofPattern("uuuu/MM/dd' 'HH:mm")));

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }

        return "startTestPage";
    }

    @PostMapping
    public String startTest(@RequestParam String testId) {

        try {
            User user = userContext.getCurrentUser();
            Profile profile = user.getProfile();

            Optional<Test> testOptional = testRepository.findById(Long.parseLong(testId))
                    .filter(Test::getActive);

            if(!testOptional.isPresent()) {
                throw new BaseException("Test with such id do not exists");
            }

            Test test = testOptional.get();

            if (testService.isUserOnTest(profile) || test.getDeadLine().isBefore(LocalDateTime.now())) {
                throw new Exception("User is busy or test expired");
            }

            TestResult testResult = new TestResult();

            testResult.setDateTime(null);
            testResult.setGrade(null);
            testResult.setStudent(profile);
            testResult.setTest(test);

            testResultRepository.save(testResult);

            testService.clearTestData(user);

            testService.startTest(user);

            return "redirect:/test/currentTest";

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }

    }

    @RequestMapping("/currentTest")
    public String getCurrentTest(Model model, @RequestParam(required = false) String question) {

        try {
            User user = userContext.getCurrentUser();
            TestResult testResult = testResultRepository.findActiveTest(user.getProfile());

            Integer questionNumber = 1;

            List<Question> questionList = testService.getQuestions(user, testResult.getTest());

            Integer numberOfQuestions = testResult.getTest().getNumberOfQuestions();

            if (question != null) {

                questionNumber = Integer.parseInt(question);

                if (questionNumber > numberOfQuestions || questionNumber < 1) {
                    throw new ArrayIndexOutOfBoundsException("Invalid question number");
                }

            }

            model.addAttribute("question", questionList.get(questionNumber - 1));
            model.addAttribute("answers", answerRepository.findByQuestion(questionList.get(questionNumber - 1)));
            model.addAttribute("questionNumber", questionNumber);

            //time tracker

            Integer timeToDeadline = testService.checkTestTime(testResult.getTest().getDuration());

            if (timeToDeadline <= 0) {

                testService.clearTestData(user);

                testResult.setDateTime(LocalDateTime.now());
                testResult.setGrade(0l);

                testResultRepository.save(testResult);

                return "redirect:/test";
            }

            model.addAttribute("timeToDeadline", timeToDeadline);
            model.addAttribute("totalTestTime", testResult.getTest().getDuration());

            //info about questions fulfil

            List<String> answers = testService.getAnswers(user);
            Map<Integer, Boolean> questionsWithResult = new LinkedHashMap<>();
            int fullPercent = convertToPercents(questionList.size(), answers.size());
            model.addAttribute("fullPercent", fullPercent);

            for (int i = 1; i <= numberOfQuestions; i++) {

                Boolean isPassed = false;

                for (String answ : answers) {

                    if (Integer.parseInt(answ.split(":")[0]) == i) {

                        isPassed = true;

                    }

                }

                questionsWithResult.put(i, isPassed);

            }

            final Integer questionNumberFinal = questionNumber;

            if (answers.size() > 0) {

                List<Integer> idOfAnswers = new LinkedList<>();

                answers.stream()
                        .filter(x -> Integer.parseInt(x.split(":")[0]) == questionNumberFinal)
                        .forEach(x -> {

                            String res = x.split(":")[1];

                            if (res.matches(".*(,).*")) {

                                for (String s : res.split(",")) {

                                    idOfAnswers.add(Integer.parseInt(s.trim()));

                                }

                            } else {
                                idOfAnswers.add(Integer.parseInt(res));
                            }

                        });

                model.addAttribute("selections", idOfAnswers);

            }

            model.addAttribute("questions", questionsWithResult);

            //basic data

            Profile profile = profileRepository.findByUserId(user);

            return "currentTestPage";

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }

    }

    private Integer convertToPercents(double total, double part) {
    	double percents = part / total * 100;
    	return (int) percents;
    }

    //TODO: max duration for test is 999

    @PostMapping("/answer")
    public String answerForQuestion(@RequestParam String question, @RequestParam(required = false) String answer) {

        try {
            User user = userContext.getCurrentUser();
            Test currentTest = testResultRepository.findActiveTest(user.getProfile()).getTest();

            Long questionPosition = Long.parseLong(question);

            if (questionPosition > 0 && questionPosition <= currentTest.getNumberOfQuestions()) {

                testService.answer(user, questionPosition + ":" + ( answer != null ? answer : -1 ) );

                if (questionPosition < currentTest.getNumberOfQuestions()) {
                    questionPosition++;
                }

                return "redirect:/test/currentTest?question=" + questionPosition;

            }

            throw new Exception("Invalid question position");

        } catch (Exception e) {
            e.printStackTrace();
            return "errorPage";
        }
    }

    @RequestMapping("/finish")
    public String finishTest(Model model) {

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();

        LocalDateTime now = LocalDateTime.now();

        if (testService.isUserOnTest(profile)) {

            TestResult testResult = testResultRepository.findActiveTest(profile);

            if (testService.checkTestTime(testResult.getTest().getDuration()) <= 0 ) {

                testResult.setGrade(0l);
                testResult.setDateTime(now);

                model.addAttribute("level", "Low");

            } else {

                Long totalPoint = 0l;

                List<Question> questions = testService.getQuestions(user, testResult.getTest());

                List<String> answers = testService.getAnswers(user);

                for (String s : answers) {

                    Integer questionPosition = Integer.parseInt(s.split(":")[0]);

                    String answer = s.split(":")[1];

                    List<Long> answersId = validationService.parseAnswers(answer);

                    List<Long> rightAnswers = validationService.parseAnswers(questions.get(questionPosition - 1).getRightAnswer());

                    if (answersId.size() == rightAnswers.size()) {

                        for (int i = 0; i < rightAnswers.size(); i++) {

                            if (!answersId.contains(rightAnswers.get(i))) {
                                break;
                            } else if (i == rightAnswers.size() - 1) {
                                totalPoint++;
                            }

                        }

                    }

                }

                testResult.setDateTime(now);
                testResult.setGrade(totalPoint);

                model.addAttribute("level", testService.getResultLevel(totalPoint.intValue(), questions.size()));

            }

            model.addAttribute("grade", testResult.getGrade());

            testResultRepository.save(testResult);

            testService.clearTestData(user);

            return "testFinishPage";

        }

        return "errorPage";

    }

    @PostMapping("/time")
    @ResponseBody
    public Map<String, Object> synchronizeTestClock(@RequestParam Integer duration) {

        HashMap<String, Object> returnCollection = new HashMap<>();

        returnCollection.put("remaind_time", testService.checkTestTime(duration));

        return returnCollection;

    }

    @RequestMapping("/grades")
    public String gradesOutput(Model model) {

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();
        List<TestResultDto> testResults = testResultRepository.findByStudent(profile,
                Sort.by(Sort.Order.asc("dateTime")))
                .stream()
                .map(testResultMapper::toDto)
                .collect(Collectors.toList());

        model.addAttribute("results", testResults);
        model.addAttribute("total", testResults.stream().mapToLong(x -> x.getGrade()).sum());
        return "gradesPage";

    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String deleteTestById(@PathVariable Long id, @RequestHeader(required = false) String referer) {
        try {
            testService.deleteById(id);
        } catch(BaseException e) {
            return "redirect:/error?message=" + e.getMessage();
        }
        return Objects.nonNull(referer) ? "redirect:" + referer : "redirect:/";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String createTestPage(Model model) {
        setMyGroups(model);
        return "createTestPage";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String createTest(TestDto testDto, Model model, HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        setMyGroups(model);
        Test newTest = null;
        try {
            newTest = toTestEntityWithQuestionsFromParams(testDto, map);
            Set<Group> newTestGroups = testService.parseGroupsByNames(testDto.getGroups());
            newTest.setGroups(newTestGroups);
        } catch(BaseException e) {
            model.addAttribute("messageError", e.getMessage());
            return "createTestPage";
        }
        try {
            testService.save(newTest);
            model.addAttribute("messageSuccess", "Test was successfully saved");
        } catch(ValidationException e) {
        	e.printFieldErrors();
            model.addAttribute("messageError", e.getMessage());
            model.mergeAttributes(e.getFieldErrors());
        }
        return "createTestPage";
    }

    private void setMyGroups(Model model) {
        Profile profile = userContext.getCurrentUser().getProfile();
        model.addAttribute("my_groups", groupRepository.findByTeacher(profile));
    }

    private Test toTestEntityWithQuestionsFromParams(TestDto testDto, Map<String, String[]> params) {
        Test entity = testDtoMapper.toEntity(testDto);
        if(Objects.isNull(entity.getActive())) {
        	entity.setActive(false);
        }
        if(Objects.nonNull(testDto.getDeadLine())) {
            LocalDateTime deadline = ParseUtils.parseDeadlineFromString(testDto.getDeadLine());
            entity.setDeadLine(deadline);
        }
        if(Objects.nonNull(testDto.getStartTime())) {
            LocalDateTime startTime = ParseUtils.parseStartTimeFromString(testDto.getStartTime());
            entity.setStartTime(startTime);
        }
        List<Question> questions = parseUtils.parseQuestionsWithAnswersFromParametersMap(params);
        entity.setQuestions(new HashSet<>(questions));
        return entity;
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String updateTestPage(@PathVariable Long id, Model model) {
        model.addAttribute("test", testService.getById(id));
        return "updateTestPage";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String updateTest(TestDto testDto, @PathVariable Long id, Model model, HttpServletRequest request) {
        Test newTest = null;
        Map<String, String[]> params = request.getParameterMap();
        try {
            newTest = toTestEntityWithQuestionsFromParams(testDto, params);
            newTest.setId(id);
            Set<Group> newTestGroups = testService.parseGroupsByNames(testDto.getGroups());
            newTest.setGroups(newTestGroups);
        } catch(BaseException e) {
            model.addAttribute("messageError", e.getMessage());
            return "updateTestPage";
        }
        try {
            testService.update(newTest);
            model.addAttribute("messageSuccess", "Test was successfully updated");
        } catch(ValidationException e) {
            model.addAttribute("messageError", e.getMessage());
            model.mergeAttributes(e.getFieldErrors());
        }
        model.addAttribute("test", newTest);
        return "updateTestPage";
    }

    @GetMapping("/active/{id}/{isActive}")
    public String activateTest(@PathVariable Long id, @PathVariable Boolean isActive,
                               @RequestHeader String referer, Model model) {
        try {
            testService.activateTest(id, isActive);
        } catch(BaseException e) {
            model.addAttribute("error_message", e.getMessage());
        }
        return "redirect:" + Optional.ofNullable(referer).orElse("/test");
    }

}
