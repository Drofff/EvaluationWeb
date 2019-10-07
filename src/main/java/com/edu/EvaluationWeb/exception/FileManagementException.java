package com.edu.EvaluationWeb.exception;

public class FileManagementException extends RuntimeException {

    public FileManagementException() {
        super();
    }

    public FileManagementException(String message) {
        super(message);
    }

    public FileManagementException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileManagementException(Throwable cause) {
        super(cause);
    }

    protected FileManagementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
