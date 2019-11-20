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
import com.edu.EvaluationWeb.dto.MarkDto;
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
				model.addAttribute("marks", toMarkDtoList(myMarks));
				model.addAttribute("selected_subject_id", subjectId);
			}
			return "marksPage";
		} catch(BaseException e) {
			return handleError(e, model);
		}
	}

	@GetMapping("/manage-students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public String manageStudents(@RequestParam(required = false) Long studentId, @RequestParam(required = false) Long subjectId, Model model) {
		if(studentId != null && subjectId != null) {
			List<Mark> marks = mySuccessService.getSubjectStatsOfStudent(subjectId, studentId);
			model.addAttribute("marks", toMarkDtoList(marks));
			model.addAttribute("selected_subject_id", subjectId);
			model.addAttribute("selected_student_id", studentId);
		}
		List<Profile> students = studentService.getMyStudents();
		model.addAttribute("students", students);
		List<Subject> subjects = subjectsService.getMySubjects();
		model.addAttribute("subjects", subjects);
		return "evaluationPage";
	}

	private List<MarkDto> toMarkDtoList(List<Mark> marks) {
		return marks.stream()
				.map(markDtoMapper::toDto)
				.collect(Collectors.toList());
	}

	@PostMapping("/evaluate")
	@PreAuthorize("hasAuthority('TEACHER')")
	public String evaluateStudent(Long studentId, Long subjectId, Long mark, String description, Model model) {
		try {
			mySuccessService.saveSuccess(mark, description, studentId, subjectId);
		} catch(BaseException e) {
			return handleError(e, model);
		}
		return "redirect:/success/manage-students?studentId=" + studentId + "&subjectId=" + subjectId;
	}

	private String handleError(BaseException e, Model model) {
		model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
		return "errorPage";
	}

}