package com.edu.EvaluationWeb.controller;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.edu.EvaluationWeb.constants.ModelConstants;
import com.edu.EvaluationWeb.dto.MessageDto;
import com.edu.EvaluationWeb.entity.Message;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.service.MessageService;

@Controller
@RequestMapping("/mail")
public class MessageController {

	private static final String DATE_TIME_MESSAGE_FORMAT = "hh:mm a dd LLL uuuu";

	private final MessageService messageService;

	@Autowired
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping("/send")
	public String sendEmail(@RequestParam(required = false) Long receiverId, Model model) {
		if(receiverId != null) {
			model.addAttribute("selectedReceiversIds", Collections.singletonList(receiverId));
		}
		model.addAttribute("my_students", messageService.getAllPossibleReceivers());
		return "messagingPage";
	}

	@PostMapping("/send")
	public String sendEmail(@RequestParam(required = false) List<Long> receivers, String title, String text, Model model) {
		try {
			messageService.sendMessage(receivers, title, text);
			model.addAttribute(ModelConstants.INFO_MESSAGE, "Email message was successfully sent");
		} catch(BaseException e) {
			model.addAttribute("selectedReceiversIds", receivers);
			model.addAttribute("oldText", text);
			model.addAttribute("oldTitle", title);
			model.addAttribute(ModelConstants.ERROR_MESSAGE_PARAM, e.getMessage());
		}
		model.addAttribute("my_students", messageService.getAllPossibleReceivers());
		return "messagingPage";
	}

	@GetMapping("/my")
	public String getMyMessages(Model model) {
		List<MessageDto> messagesToMe = prepareMessageList(messageService.getMyMessages());
		model.addAttribute("to_me_messages", messagesToMe);
		List<MessageDto> messagesFromMe = prepareMessageList(messageService.getMessagesFromMe());
		model.addAttribute("from_me_messages", messagesFromMe);
		return "myMessagesPage";
	}

	private MessageDto toMessageDto(Message message) {
		MessageDto messageDto = new MessageDto(message);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_MESSAGE_FORMAT);
		String formattedDateTime = message.getDateTime().format(formatter);
		messageDto.setDateTimeFormatted(formattedDateTime);
		return messageDto;
	}

	private List<MessageDto> prepareMessageList(List<Message> messageList) {
		ZoneOffset zoneOffset = ZonedDateTime.now().getOffset();
		return messageList.stream()
				.sorted(Comparator.comparingInt(msg -> -(int) msg.getDateTime().toEpochSecond(zoneOffset)))
				.map(this::toMessageDto)
				.collect(Collectors.toList());
	}

}
