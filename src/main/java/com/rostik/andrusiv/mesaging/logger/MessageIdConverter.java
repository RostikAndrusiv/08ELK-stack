package com.rostik.andrusiv.mesaging.logger;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicLong;

public class MessageIdConverter extends ClassicConverter {
    private static final AtomicLong ATOMIC_COUNTER = new AtomicLong();

    @Override
    public String convert(final ILoggingEvent event) {
        String currentCounter = String.valueOf(ATOMIC_COUNTER.getAndIncrement());
        String padded = String.format("%9s", currentCounter).replace(' ', '0');
        StringBuilder sb = new StringBuilder(padded);
        sb.insert(2,"-");
        sb.insert(5,"-");
        return sb.toString();
    }
}
