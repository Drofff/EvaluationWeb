package com.drofff.edu.service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    private final Validator validator;

    @Autowired
    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public <T> Map<String, String> validate(T t) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        Map<String, String> result = new HashMap<>();
        constraintViolations.forEach(x -> {
            result.put(x.getPropertyPath() + "Error", x.getMessage());
        });
        return result;
    }

    public <T> Map<String, String> validateRecursively(T t) {
        Map<String, String> errors = validate(t);
        if(errors.isEmpty()) {
            Field [] fields = t.getClass().getDeclaredFields();
            List<Collection> collectionFields = Arrays.stream(fields)
                    .filter(field -> Collection.class.isAssignableFrom(field.getType()))
                    .map(field -> getFieldValue(t, field, Collection.class))
                    .collect(Collectors.toList());
            return collectionFields.stream()
                    .map(this::validateCollectionRecursively)
                    .findFirst().orElse(Collections.emptyMap());
        }
        return errors;
    }

    private Map<String, String> validateCollectionRecursively(Collection<?> collection) {
        return collection.stream()
                .map(this::validateRecursively)
                .filter(elem -> !elem.isEmpty())
                .findFirst().orElse(Collections.emptyMap());
    }

    private <T> T getFieldValue(Object object, Field field, Class<T> resultType) {
        try {
            field.setAccessible(true);
            Object value = field.get(object);
            return resultType.cast(value);
        } catch(IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
