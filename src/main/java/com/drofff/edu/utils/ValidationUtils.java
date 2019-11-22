package com.drofff.edu.utils;

import com.drofff.edu.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

public class ValidationUtils {

    private static final String DEFAULT_NON_NULL_ERROR_MESSAGE = "Provided data is null";
    private static final String DEFAULT_FILE_SIZE_ERROR_MESSAGE = "Maximum allowed file size is 5Mb";
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5;

	private static final Integer MAX_EMAIL_TEXT_SIZE = 255;
	private static final Integer MIN_EMAIL_TEXT_SIZE = 1;

	private static final Integer MAX_EMAIL_TOPIC_SIZE = 40;
	private static final Integer MIN_EMAIL_TOPIC_SIZE = 1;

	private static final String EMAIL_ADDRESS_PATTERN  = ".+@.+\\..+";

    public static void validateNonNull(Object object) {
        validateNonNull(object, DEFAULT_NON_NULL_ERROR_MESSAGE);
    }

    public static void validateNonNull(Object object, String errorMessage) {
        if(Objects.isNull(object)) {
            throw new BaseException(errorMessage);
        }
    }

    public static void validateFileSize(MultipartFile file) {
        validateFileSize(file, DEFAULT_FILE_SIZE_ERROR_MESSAGE);
    }

    public static void validateFileSize(MultipartFile file, String errorMessage) {
        long fileSizeInBytes = file.getSize();
        if(fileSizeInBytes > MAX_FILE_SIZE) {
            throw new BaseException(errorMessage);
        }
    }

	public static void validateMail(List<String> email, String title, String text) {
		validateEmailAddresses(email);
		validateSize(title, "title", MIN_EMAIL_TOPIC_SIZE, MAX_EMAIL_TOPIC_SIZE);
		validateSize(text, "text", MIN_EMAIL_TEXT_SIZE, MAX_EMAIL_TEXT_SIZE);
	}

	private static void validateEmailAddresses(List<String> emails) {
    	if(emails == null || emails.isEmpty()) {
    		throw new BaseException("Please, provide at least one email address");
	    }
    	emails.forEach(ValidationUtils::validateEmailAddress);
	}

	private static void validateEmailAddress(String email) {
		validateNotNull(email, "email address");
		if(!ParseUtils.matches(email, EMAIL_ADDRESS_PATTERN)) {
			throw new BaseException("Invalid email address format");
		}
	}

	private static void validateSize(String text, String fieldName, int minSize, int maxSize) {
		validateNotNull(text, fieldName);
		if(text.length() < minSize || text.length() > maxSize) {
			throw new BaseException("Value of " + fieldName + " is out of bounds. Min length is " + minSize + " and max length is " + maxSize);
		}
	}

	private static void validateNotNull(String text, String fieldName) {
		if(Objects.isNull(text)) {
			throw new BaseException("Please, provide valid " + fieldName);
		}
	}

}
