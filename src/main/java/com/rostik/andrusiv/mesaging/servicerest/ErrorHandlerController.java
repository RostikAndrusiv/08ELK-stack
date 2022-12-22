package com.rostik.andrusiv.mesaging.servicerest;

import com.rostik.andrusiv.mesaging.exception.ServiceException;
import com.rostik.andrusiv.mesaging.servicedto.model.ErrorType;
import com.rostik.andrusiv.mesaging.servicedto.model.EventError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerController {

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EventError handleServiceException(ServiceException ex, HandlerMethod hm) {
        log.error("handleServiceException: message: {}, method: {}", ex.getMessage(),
                hm.getMethod().getName(), ex);
        return new EventError(ex.getMessage(), ex.getErrorType(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EventError handleException(Exception ex, HandlerMethod hm) {
        log.error("handleException: message: {}, method: {}", ex.getMessage(),
                hm.getMethod().getName(), ex);
        return new EventError(ex.getMessage(), ErrorType.FATAL_ERROR_TYPE, LocalDateTime.now());
    }
}
