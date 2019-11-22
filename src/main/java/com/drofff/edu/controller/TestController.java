package com.drofff.edu.controller;

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

import com.drofff.edu.component.UserContext;
import com.drofff.edu.constants.ModelConstants;
import com.drofff.edu.dto.AnswerDto;
import com.drofff.edu.dto.ManageTestDto;
import com.drofff.edu.dto.QuestionDto;
import com.drofff.edu.dto.TestDto;
import com.drofff.edu.dto.TestResultDto;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.Question;
import com.drofff.edu.entity.Test;
import com.drofff.edu.entity.TestResult;
import com.drofff.edu.entity.User;
import com.drofff.edu.exception.BaseException;
import com.drofff.edu.exception.ValidationException;
import com.drofff.edu.mapper.TestDtoMapper;
import com.drofff.edu.mapper.TestResultMapper;
import com.drofff.edu.repository.AnswerRepository;
import com.drofff.edu.repository.GroupRepository;
import com.drofff.edu.repository.ProfileRepository;
import com.drofff.edu.repository.TestRepository;
import com.drofff.edu.repository.TestResultRepository;
import com.drofff.edu.service.FilesService;
import com.drofff.edu.service.TestService;
import com.drofff.edu.utils.ClosestTestComparator;
import com.drofff.edu.utils.ParseUtils;
import com.drofff.edu.utils.TestUtils;

@Controller
@RequestMapping("/test")
public class TestController {

	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd' 'LLLL' ('hh':'mm')'");

    private final ProfileRepository profileRepository;
    private final TestService testService;
    private final TestRepository testRepository;
    private final TestResultRepository testResultRepository;
    private final AnswerRepository answerRepository;
    private final TestResultMapper testResultMapper;
    private final ParseUtils parseUtils;
    private final TestDtoMapper testDtoMapper;
    private final UserContext userContext;
    private final GroupRepository groupRepository;
    private final FilesService filesService;

    @Autowired
    public TestController(ProfileRepository profileRepository, TestService testService,
                          TestRepository testRepository, TestResultRepository testResultRepository,
                          AnswerRepository answerRepository, TestResultMapper testResultMapper,
                          ParseUtils parseUtils, TestDtoMapper testDtoMapper, UserContext userContext,
                          GroupRepository groupRepository, FilesService filesService) {
        this.profileRepository = profileRepository;
        this.testService = testService;

        this.testRepository = testRepository;
        this.testResultRepository = testResultRepository;
        this.answerRepository = answerRepository;
        this.testResultMapper = testResultMapper;
        this.parseUtils = parseUtils;
        this.testDtoMapper = testDtoMapper;
        this.userContext = userContext;
        this.groupRepository = groupRepository;
	    this.filesService = filesService;
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
			fetchTestTeacherPhoto(test);
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

    private void fetchTestTeacherPhoto(Test test) {
    	Profile teacher = test.getTeacher();
    	String photo = filesService.loadPhoto(teacher.getPhotoUrl());
    	teacher.setPhotoUrl(photo);
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

                    List<Long> answersId = ParseUtils.parseAnswersString(answer);

                    List<Long> rightAnswers = ParseUtils.parseAnswersString(questions.get(questionPosition - 1).getRightAnswer());

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
        model.addAttribute("total", testResults.stream().mapToLong(TestResultDto::getGrade).sum());
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
	    Map<String, String[]> params = request.getParameterMap();
	    model.addAttribute("oldParams", TestUtils.toSingleValueParamsMap(params));
	    List<AnswerDto> additionalAnswers = TestUtils.getAdditionalAnswersForFirstQuestionFromParams(params);
	    model.addAttribute("firstQuestionAdditionalAnswers", additionalAnswers);
	    setMyGroups(model);
	    try {
	        Test test = buildTest(testDto, params);
	        model.addAttribute("enteredValue", test);
	        model.addAttribute("selectedGroupsNames", testDto.getGroups());
            testService.save(test);
            model.addAttribute("messageSuccess", "Test was successfully saved");
        } catch(ValidationException e) {
        	e.printFieldErrors();
            model.addAttribute("messageError", e.getMessage());
            model.mergeAttributes(e.getFieldErrors());
        } catch(BaseException e) {
	        model.addAttribute("messageError", e.getMessage());
        }
        return "createTestPage";
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String updateTestPage(@PathVariable Long id, Model model) {
    	Test test = testService.getById(id);
    	putTestIntoModel(test, model);
	    setMyGroups(model);
        return "editTestPage";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String updateTest(TestDto testDto, @PathVariable Long id, HttpServletRequest request, Model model) {
        try {
        	Test test = buildTest(id, testDto, request.getParameterMap());
        	putTestIntoModel(test, model);
            testService.update(test);
            model.addAttribute(ModelConstants.INFO_MESSAGE, "Test was successfully updated");
        } catch(ValidationException e) {
            model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
            model.mergeAttributes(e.getFieldErrors());
        } catch(BaseException e) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
        }
        setMyGroups(model);
        return "editTestPage";
    }

    private void putTestIntoModel(Test test, Model model) {
	    Set<QuestionDto> questions = TestUtils.getQuestionDtoSet(test);
	    model.addAttribute("questions", questions);
	    model.addAttribute("lastQuestionIndex", questions.size());
	    model.addAttribute("test", test);
	    model.addAttribute("testId", test.getId());
	    List<String> groupNames = TestUtils.toGroupNamesList(test.getGroups());
	    model.addAttribute("selectedGroupsNames", groupNames);
    }

    private Test buildTest(Long id, TestDto testDto, Map<String, String[]> params) {
    	Test test = buildTest(testDto, params);
	    test.setId(id);
	    return test;
    }

    private Test buildTest(TestDto testDto, Map<String, String[]> params) {
	    Test test = testDtoMapper.toEntity(testDto);
	    List<Question> questions = parseUtils.parseQuestionsWithAnswersFromParametersMap(params);
	    test.setQuestions(new HashSet<>(questions));
	    Set<Group> groups = testService.parseGroupsByNames(testDto.getGroups());
	    test.setGroups(groups);
	    return test;
    }

	private void setMyGroups(Model model) {
		Profile profile = userContext.getCurrentUser().getProfile();
		model.addAttribute("my_groups", groupRepository.findByTeacher(profile));
	}

    @GetMapping("/manage")
	public String manageMyTests(Optional<Integer> page, Model model,
                                @RequestParam(name = ModelConstants.ERROR_MESSAGE_PARAM, required = false) String errorMessage) {
	    model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, errorMessage);
    	List<Test> tests = testService.getMyOwnTests(page.orElse(0));
    	model.addAttribute("tests", tests.stream()
			    .sorted(new ClosestTestComparator())
		        .map(this::toManageTestDto)
		        .collect(Collectors.toList()));
    	return "manageTests";
    }

    private ManageTestDto toManageTestDto(Test test) {
	    TestDto testDto = testDtoMapper.toDto(test);
	    ManageTestDto manageTestDto = new ManageTestDto(testDto);
	    manageTestDto.setId(test.getId());
	    manageTestDto.setNumberOfQuestions(test.getNumberOfQuestions());
	    manageTestDto.setStartTime(test.getStartTime().format(DATE_TIME_FORMAT));
	    manageTestDto.setDeadLine(test.getDeadLine().format(DATE_TIME_FORMAT));
	    return manageTestDto;
    }

	@GetMapping("/active/{id}/{isActive}")
	public String activateTest(@PathVariable Long id, @PathVariable Boolean isActive,
	                           Model model) {
    	String redirect = "redirect:/test/manage";
		try {
			testService.activateTest(id, isActive);
			return redirect;
		} catch(BaseException e) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			return redirect + "?" + ModelConstants.ERROR_MESSAGE_PARAM + "=" + e.getMessage();
		}
	}

}
