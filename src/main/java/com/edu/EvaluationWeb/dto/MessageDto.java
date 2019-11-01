package com.edu.EvaluationWeb.dto;

import com.edu.EvaluationWeb.entity.Message;

public class MessageDto extends Message {

	public MessageDto(Message message) {
		super.copyFieldValuesFrom(message);
	}

	private String dateTimeFormatted;

	public String getDateTimeFormatted() {
		return dateTimeFormatted;
	}

	public void setDateTimeFormatted(String dateTimeFormatted) {
		this.dateTimeFormatted = dateTimeFormatted;
	}

}
