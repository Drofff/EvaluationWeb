package com.drofff.edu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drofff.edu.entity.Mark;
import com.drofff.edu.component.UserContext;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.Subject;
import com.drofff.edu.exception.BaseException;
import com.drofff.edu.repository.MarkRepository;
import com.drofff.edu.repository.ProfileRepository;
import com.drofff.edu.repository.SubjectRepository;

@Service
public class MySuccessService {

	private static final String INVALID_STUDENT_ID_MESSAGE = "Invalid student id";
	private static final String INVALID_SUBJECT_ID_MESSAGE = "Invalid subject id";
	private static final Integer MAX_DESCRIPTION_LENGTH = 20;

	private final MarkRepository markRepository;
	private final ProfileRepository profileRepository;
	private final UserContext userContext;
	private final SubjectRepository subjectRepository;

	@Autowired
	public MySuccessService(MarkRepository markRepository, ProfileRepository profileRepository,
	                        UserContext userContext, SubjectRepository subjectRepository) {
		this.markRepository = markRepository;
		this.profileRepository = profileRepository;
		this.userContext = userContext;
		this.subjectRepository = subjectRepository;
	}

	public void saveSuccess(Long mark, String description, Long studentId, Long subjectId) {
		Profile student = profileRepository.findById(studentId)
				.orElseThrow(() -> new BaseException(INVALID_STUDENT_ID_MESSAGE));
		Profile teacher = userContext.getCurrentUser().getProfile();
		validateIsTeacherOfStudent(teacher, student);
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new BaseException(INVALID_SUBJECT_ID_MESSAGE));
		validateIsTeacherOfSubject(teacher, subject);
		validateMark(mark);
		validateDescription(description);
		Mark markObject = buildMark(mark, student, subject, description);
		markRepository.save(markObject);
	}

	private void validateIsTeacherOfStudent(Profile teacher, Profile student) {
		Group studentGroup = student.getGroup();
		if(!studentGroup.getTeachers().contains(teacher)) {
			throw new BaseException("Student can not be evaluated");
		}
	}

	private void validateIsTeacherOfSubject(Profile teacher, Subject subject) {
		Long currentUserId = teacher.getUserId().getId();
		if(!subject.getTeacher().getId().equals(currentUserId)) {
			throw new BaseException("Subject can not be evaluated");
		}
	}

	private void validateMark(Long mark) {
		if(mark < 0) {
			throw new BaseException("Mark can not be negative");
		}
	}

	private void validateDescription(String description) {
		if(description.isEmpty() || description.length() > MAX_DESCRIPTION_LENGTH) {
			throw new BaseException("Description should be provided with length no bigger than " + MAX_DESCRIPTION_LENGTH + " characters");
		}
	}

	private Mark buildMark(Long mark, Profile student, Subject subject, String description) {
		Mark markObject = new Mark();
		markObject.setMark(mark);
		markObject.setStudent(student);
		markObject.setSubject(subject);
		markObject.setLocalDateTime(LocalDateTime.now());
		markObject.setDescription(description);
		return markObject;
	}

	public List<Mark> getMyStatsOfSubject(Long subjectId) {
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new BaseException(INVALID_SUBJECT_ID_MESSAGE));
		Profile profile = userContext.getCurrentUser().getProfile();
		return markRepository.findByStudentAndSubject(profile, subject);
	}

	public List<Mark> getSubjectStatsOfStudent(Long subjectId, Long studentId) {
		Profile teacher = userContext.getCurrentUser().getProfile();
		Profile student = profileRepository.findById(studentId)
				.orElseThrow(() -> new BaseException(INVALID_STUDENT_ID_MESSAGE));
		validateIsTeacherOfStudent(teacher, student);
		Subject subject = subjectRepository.findById(subjectId)
				.orElseThrow(() -> new BaseException(INVALID_SUBJECT_ID_MESSAGE));
		validateIsTeacherOfSubject(teacher, subject);
		return markRepository.findByStudentAndSubject(student, subject);
	}

	public List<Subject> getMyStudyingSubjects() {
		Profile profile = userContext.getCurrentUser().getProfile();
		return markRepository.getStudyingSubjects(profile);
	}

}
