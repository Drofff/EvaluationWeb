package com.edu.EvaluationWeb.exception;

public class InvalidPermissionException extends StorageException {

    private static final String ERROR_MESSAGE = "Invalid permission while attempting to reach ";

    public InvalidPermissionException(String path) {
        super(ERROR_MESSAGE + path);
    }

}
