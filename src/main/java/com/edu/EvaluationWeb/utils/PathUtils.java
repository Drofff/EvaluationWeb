package com.edu.EvaluationWeb.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathUtils {

    public static String extractStoragePath(String mappingRegex, String requestUri) {
        String path = UriComponentsBuilder.fromUriString(requestUri)
                .build()
                .getPath();
        if(Objects.nonNull(path)) {
            Matcher matcher = Pattern.compile(mappingRegex).matcher(path);
            if(matcher.find()) {
                return path.replace(matcher.group(), "");
            }
        }
        return null;
    }

    public static String extractCurrentDirectory(String path) {
        String [] pathSegments = path.split("/");
        return pathSegments[pathSegments.length - 1];
    }

    public static Map<String, String> extractNavigationInfo(String path, String currentUri) {
        if(path == null) {
            return null;
        }
        String [] pathSegments = path.split("/");
        String [] dirContext = Arrays.copyOf(pathSegments, pathSegments.length - 1);
        Map<String, String> navigation = new HashMap<>();
        Arrays.stream(dirContext).forEach(x -> {
            String pathContext = path.split(x, 2)[0];
            String url = currentUri + (pathContext.isEmpty() ? "" : "/" + pathContext) + "/" + x;
            navigation.put(x, url);
        });
        return navigation;
    }

}
