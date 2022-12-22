package com.rostik.andrusiv.mesaging.exception;

import com.rostik.andrusiv.mesaging.servicedto.model.ErrorType;

public class IndexException extends ServiceException {
    private static final String DEFAULT_MESSAGE = "Index is not created";

    public IndexException() {
        super(DEFAULT_MESSAGE);
    }

    public IndexException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.PROCESSING_ERROR_TYPE;
    }
}
