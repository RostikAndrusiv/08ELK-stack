package com.rostik.andrusiv.mesaging.exception;

import com.rostik.andrusiv.mesaging.servicedto.model.ErrorType;

public class ClientException extends ServiceException {
    private static final String DEFAULT_MESSAGE = "client error";

    public ClientException() {
        super(DEFAULT_MESSAGE);
    }

    public ClientException(String message) {
        super(message);
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.FATAL_ERROR_TYPE;
    }
}
