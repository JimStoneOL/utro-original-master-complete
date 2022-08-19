package com.example.utro.annotations;
import com.example.utro.validations.UUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(ElementType.FIELD)
@Constraint(validatedBy={UUIDValidator.class})
@Retention(RUNTIME)
public @interface ValidUUID {
    String message() default "Invalid uuid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}