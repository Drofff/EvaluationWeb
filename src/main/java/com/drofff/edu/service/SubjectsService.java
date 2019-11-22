package com.drofff.edu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.edu.entity.Subject;
import com.drofff.edu.entity.User;
import com.drofff.edu.repository.SubjectRepository;
import com.drofff.edu.component.UserContext;
import com.drofff.edu.exception.BaseException;

@Service
public class SubjectsService {

	private final SubjectRepository subjectRepository;
	private final UserContext userContext;

	@Autowired
	public SubjectsService(SubjectRepository subjectRepository, UserContext userContext) {
		this.subjectRepository = subjectRepository;
		this.userContext = userContext;
	}

	public List<Subject> getMySubjects() {
		User currentUser = userContext.getCurrentUser();
		return subjectRepository.findByTeacher(currentUser);
	}

	public void createSubject(String name) {
		if(name == null || name.isEmpty()) {
			throw new BaseException("Invalid name was provided");
		}
		User currentUser = userContext.getCurrentUser();
		Subject subject = createSubjectWithNameAndTeacher(name, currentUser);
		subjectRepository.save(subject);
	}

	private Subject createSubjectWithNameAndTeacher(String name, User teacher) {
		Subject subject = new Subject();
		subject.setName(name);
		subject.setTeacher(teacher);
		return subject;
	}

	public void deleteSubjectById(Long id) {
		Subject subject = subjectRepository.findById(id)
				.orElseThrow(() -> new BaseException("Subject with such id do not exists"));
		validatePermission(subject);
		subjectRepository.delete(subject);
	}

	private void validatePermission(Subject subject) {
		if(!subject.getTeacher().getId().equals(userContext.getCurrentUser().getId())) {
			throw new BaseException("Invalid permission");
		}
	}

}
