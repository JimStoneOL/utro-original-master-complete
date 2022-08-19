package com.example.utro.validations;

import com.example.utro.annotations.ValidUUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDValidator implements ConstraintValidator<ValidUUID, UUID> {

    public static final String UUID_PATTERN="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    @Override
    public void initialize(ValidUUID constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UUID uuid, ConstraintValidatorContext constraintValidatorContext) {
        return validateUUID(uuid.toString());
    }
    private boolean validateUUID(String uuid){
        Pattern pattern=Pattern.compile(UUID_PATTERN);
        Matcher matcher=pattern.matcher(uuid);
        return matcher.matches();
    }
}
