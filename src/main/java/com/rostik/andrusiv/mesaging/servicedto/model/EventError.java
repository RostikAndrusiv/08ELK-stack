package com.rostik.andrusiv.mesaging.servicedto.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventError {

    private String message;

    private ErrorType errorType;

    private LocalDateTime timeStamp;

}
