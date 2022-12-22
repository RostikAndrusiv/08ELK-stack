package com.rostik.andrusiv.mesaging.exception;

import com.rostik.andrusiv.mesaging.servicedto.model.ErrorType;

public class EventException extends ServiceException {
    private static final String DEFAULT_MESSAGE = "general exception";

    public EventException() {
        super(DEFAULT_MESSAGE);
    }

    public EventException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.PROCESSING_ERROR_TYPE;
    }
}
