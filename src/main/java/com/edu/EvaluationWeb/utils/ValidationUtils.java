package com.edu.EvaluationWeb.utils;

import com.edu.EvaluationWeb.exception.BaseException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class ValidationUtils {

    private static final String DEFAULT_NON_NULL_ERROR_MESSAGE = "Provided data is null";
    private static final String DEFAULT_FILE_SIZE_ERROR_MESSAGE = "Maximum allowed file size is 5Mb";
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 5;

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

}
