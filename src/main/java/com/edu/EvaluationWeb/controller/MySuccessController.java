package com.edu.EvaluationWeb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.EvaluationWeb.constants.ModelConstants;
import com.edu.EvaluationWeb.entity.Mark;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.Subject;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.mapper.MarkDtoMapper;
import com.edu.EvaluationWeb.service.MySuccessService;
import com.edu.EvaluationWeb.service.StudentService;
import com.edu.EvaluationWeb.service.SubjectsService;

@Controller
@RequestMapping("/success")
public class MySuccessController {

	private final MySuccessService mySuccessService;
	private final StudentService studentService;
	private final SubjectsService subjectsService;
	private final MarkDtoMapper markDtoMapper;

	@Autowired
	public MySuccessController(MySuccessService mySuccessService, StudentService studentService,
	                           SubjectsService subjectsService, MarkDtoMapper markDtoMapper) {
		this.mySuccessService = mySuccessService;
		this.studentService = studentService;
		this.subjectsService = subjectsService;
		this.markDtoMapper = markDtoMapper;
	}

	@GetMapping("/my")
	public String getMySuccessStats(@RequestParam(required = false) Long subjectId, Model model) {
		List<Subject> mySubjects = mySuccessService.getMyStudyingSubjects();
		model.addAttribute("subjects", mySubjects);
		try {
			if(subjectId != null) {
				List<Mark> myMarks = mySuccessService.getMyStatsOfSubject(subjectId);
				model.addAttribute("marks", myMarks.stream()
						.map(markDtoMapper::toDto)
						.collect(Collectors.toList()));
				model.addAttribute("selected_subject_id", subjectId);
			}
			return "marksPage";
		} catch(BaseException e) {
			return handleError(e, model);
		}
	}

	@GetMapping("/manage-students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public String manageStudents(Model model) {
		List<Profile> students = studentService.getMyStudents();
		model.addAttribute("students", students);
		List<Subject> subjects = subjectsService.getMySubjects();
		model.addAttribute("subjects", subjects);
		return "evaluationPage";
	}

	@GetMapping("/student/{id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public String getStudentSuccessStats(@PathVariable Long id, Long subjectId, Model model) {
		try {
			List<Mark> marks = mySuccessService.getSubjectStatsOfStudent(subjectId, id);
			model.addAttribute("marks", marks);
			return "studentsMarksPage";
		} catch(BaseException e) {
			return handleError(e, model);
		}
	}

	@PostMapping("/evaluate/{studentId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public String evaluateStudent(@PathVariable Long studentId, Long subjectId, Long mark, String description, Model model) {
		try {
			mySuccessService.saveSuccess(mark, description, studentId, subjectId);
		} catch(BaseException e) {
			return handleError(e, model);
		}
		return "redirect:/evaluate";
	}

	private String handleError(BaseException e, Model model) {
		model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
		return "errorPage";
	}

}