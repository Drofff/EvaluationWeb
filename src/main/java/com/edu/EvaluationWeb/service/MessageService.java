package com.edu.EvaluationWeb.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Message;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.repository.MessageRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import com.edu.EvaluationWeb.repository.UserRepository;
import com.edu.EvaluationWeb.utils.ValidationUtils;

@Service
public class MessageService {

	private final MailService mailService;
	private final UserRepository userRepository;
	private final MessageRepository messageRepository;
	private final UserContext userContext;
	private final ProfileRepository profileRepository;

	private static final String NEW_LINE_HTML_SYMBOL = "<br/>";
	private static final String NEW_LINE_JAVA_SYMBOL = "\n";
	private static final String MESSAGE_AUTHOR_PREFIX = "Message from ";

	@Autowired
	public MessageService(MailService mailService, UserRepository userRepository,
	                      MessageRepository messageRepository, UserContext userContext,
	                      ProfileRepository profileRepository) {
		this.mailService = mailService;
		this.userRepository = userRepository;
		this.messageRepository = messageRepository;
		this.userContext = userContext;
		this.profileRepository = profileRepository;
	}

	public List<Message> getMyMessages() {
		User user = userContext.getCurrentUser();
		return messageRepository.findByReceiver(user);
	}

	public List<Message> getMessagesFromMe() {
		User user = userContext.getCurrentUser();
		return messageRepository.findBySender(user);
	}

	public void sendMessage(List<Long> userIds, String topic, String text) {
		List<User> users = getUsersByIds(userIds);
		List<String> emails = getEmails(users);
		ValidationUtils.validateMail(emails, topic, text);
		User currentUser = userContext.getCurrentUser();
		String message = buildMessage(text, currentUser);
		mailService.sendEmail(topic, message, emails.toArray(new String[]{}));
		saveToDatabase(users, topic, text, currentUser);
	}

	private List<User> getUsersByIds(List<Long> userIds) {
		if(userIds != null) {
			return userIds.stream()
					.distinct()
					.map(userRepository::findById)
					.map(user -> user.orElseThrow(() -> new BaseException("User with such id do not exists")))
					.collect(Collectors.toList());
		}
		throw new BaseException("Please, select mail receivers");
	}

	private List<String> getEmails(List<User> userList) {
		return userList.stream()
				.map(User::getUsername)
				.collect(Collectors.toList());
	}

	private String buildMessage(String text, User author) {
		String textWithAuthorInfo = addAuthorInfo(text, author);
		return convertToHtmlText(textWithAuthorInfo);
	}

	private String addAuthorInfo(String text, User author) {
		String authorName = author.getProfile().getFirstName() + " " + author.getProfile().getLastName();
		return MESSAGE_AUTHOR_PREFIX + authorName + ":<br/><br/>" + text;
	}

	private String convertToHtmlText(String text) {
		return text.replaceAll(NEW_LINE_JAVA_SYMBOL, NEW_LINE_HTML_SYMBOL);
	}

	private void saveToDatabase(List<User> receivers, String topic, String text, User sender) {
		Message message = new Message();
		message.setReceivers(receivers);
		message.setTopic(topic);
		message.setText(text);
		message.setDateTime(LocalDateTime.now());
		message.setSender(sender);
		messageRepository.save(message);
	}

	public List<Profile> getAllPossibleReceivers() {
		Profile currentUserProfile = userContext.getCurrentUser().getProfile();
		Group myGroup = currentUserProfile.getGroup();
		List<Profile> receivers = getAllUsersFromGroupDistinct(myGroup);
		setMyStudentsToReceiversIfTeacher(currentUserProfile, receivers);
		receivers.remove(currentUserProfile);
		return receivers;
	}

	private List<Profile> getAllUsersFromGroupDistinct(Group group) {
		List<Profile> members = profileRepository.findByGroup(group);
		List<Profile> teachers = profileRepository.findAllTeachersByGroupId(group.getId());
		members.addAll(teachers);
		return members.stream()
				.distinct()
				.collect(Collectors.toList());
	}

	private void setMyStudentsToReceiversIfTeacher(Profile profile, List<Profile> receivers) {
		if(profile.getUserId().isTeacher()) {
			List<Profile> myStudents = profileRepository.findByTeacher(profile);
			receivers.addAll(myStudents);
		}
	}

}