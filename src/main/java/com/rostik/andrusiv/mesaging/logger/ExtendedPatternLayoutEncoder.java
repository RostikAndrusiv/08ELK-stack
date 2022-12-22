package com.rostik.andrusiv.mesaging.logger;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;

public class ExtendedPatternLayoutEncoder extends PatternLayoutEncoder {
    @Override
    public void start() {
        PatternLayout.DEFAULT_CONVERTER_MAP.put(
                "message_id", MessageIdConverter.class.getName());
        super.start();
    }
}
