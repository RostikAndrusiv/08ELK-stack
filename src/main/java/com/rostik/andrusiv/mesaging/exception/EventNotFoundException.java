package com.rostik.andrusiv.mesaging.exception;

import com.rostik.andrusiv.mesaging.servicedto.model.ErrorType;

public class EventNotFoundException extends ServiceException {
    private static final String DEFAULT_MESSAGE = "event not found";

    public EventNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DATABASE_ERROR_TYPE;
    }
}

