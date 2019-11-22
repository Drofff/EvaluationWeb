package com.drofff.edu.utils;

import java.util.Iterator;
import java.util.Map;

public class MailUtils {

    public static String fillMarkers(Iterator<Map.Entry<String, String>> values, String message) {
        if(!values.hasNext()) {
            return message;
        }
        Map.Entry<String, String> value = values.next();
        return fillMarkers(values, message.replace(value.getKey(), value.getValue()));
    }

}
