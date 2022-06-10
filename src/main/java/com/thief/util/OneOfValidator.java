package com.thief.util;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

@Component
public class OneOfValidator implements ConstraintValidator<OneOf, Object> {

    private String[] fields;

    @Override
    public void initialize(final OneOf combinedOf) {
        fields = combinedOf.fields();
    }

    @Override
    public boolean isValid(final Object value, ConstraintValidatorContext context) {
        final BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);

        return Arrays.stream(fields)
                .map(beanWrapper::getPropertyValue)
                .filter(Objects::isNull)
                .count() == 1;
    }
}
