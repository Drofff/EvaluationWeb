package com.drofff.edu.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.drofff.edu.service.SubjectsService;
import com.drofff.edu.constants.ModelConstants;
import com.drofff.edu.exception.BaseException;

@Controller
@RequestMapping("/subjects")
@PreAuthorize("hasAuthority('TEACHER')")
public class SubjectsController {

	private final SubjectsService subjectsService;

	@Autowired
	public SubjectsController(SubjectsService subjectsService) {
		this.subjectsService = subjectsService;
	}

	@GetMapping
	public String getMySubjects(Model model) {
		model.addAttribute("subjects", subjectsService.getMySubjects());
		return "mySubjectsPage";
	}

	@GetMapping("/create")
	public String createSubjectPage() {
		return "createSubjectPage";
	}

	@PostMapping("/create")
	public String createSubject(String name, Model model) {
		try {
			subjectsService.createSubject(name);
			model.addAttribute(ModelConstants.INFO_MESSAGE, "Successfully created subject with name " + name);
		} catch(BaseException e) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
		}
		return "createSubjectPage";
	}

	@GetMapping("/delete/{id}")
	public String deleteSubject(@PathVariable Long id, Model model, @RequestHeader String referer) {
		try {
			subjectsService.deleteSubjectById(id);
		} catch(BaseException e) {
			model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
			return "errorPage";
		}
		return "redirect:" + ( Objects.nonNull(referer) ? referer : "/subjects" );
	}

}
