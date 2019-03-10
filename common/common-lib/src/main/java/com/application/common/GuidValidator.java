package com.application.common;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.web.context.WebApplicationContext;

@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GuidValidator implements ConstraintValidator<Guid, Object>{

    @Autowired
    HttpServletRequest request;

    @Override
    public void initialize(Guid constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(request.getMethod().equalsIgnoreCase(HttpMethod.POST.toString())) {
            return value != null;
        }
        return true;
    }

}
