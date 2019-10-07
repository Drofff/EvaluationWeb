package com.edu.EvaluationWeb.exception;

public class NoSuchDirException extends StorageException {

    private static final String ERROR_MESSAGE = "Directory on path %s do not exists";

    public NoSuchDirException(String path) {
        super(String.format(ERROR_MESSAGE, path));
    }

}
