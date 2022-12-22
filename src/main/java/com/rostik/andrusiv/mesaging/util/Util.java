package com.rostik.andrusiv.mesaging.util;

import com.rostik.andrusiv.mesaging.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.nio.file.Files;

@Slf4j
public class Util {

    private Util() {
    }

    public static String loadAsString(final String path) {
        try {
            final File resource = new ClassPathResource(path).getFile();

            return new String(Files.readAllBytes(resource.toPath()));
        } catch (final Exception e) {
            throw new ServiceException("can't read from file");
        }
    }
}
