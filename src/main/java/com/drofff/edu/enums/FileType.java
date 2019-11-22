package com.drofff.edu.enums;

import org.springframework.core.env.Environment;

public enum FileType {
    PHOTO {
        @Override
        public String getBasePath(Environment environment) {
            return environment.getProperty("photo.dir.path");
        }
    };

    public abstract String getBasePath(Environment environment);
}
