package com.application.common;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = GuidValidator.class)
@Pattern(regexp=CommonConstants.GUID_REGEX, message="Invalid")
public @interface Guid {

    String message() default "Guid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
