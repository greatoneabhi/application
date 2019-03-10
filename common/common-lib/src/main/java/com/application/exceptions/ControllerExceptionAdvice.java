package com.application.exceptions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.application.logger.Loggable;

@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    @Loggable
    public ErrorResponse handleConflict(DataIntegrityViolationException e) {
        return new ErrorResponse( HttpServletResponse.SC_CONFLICT,
                HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    @Loggable
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException e) {
        return new ErrorResponse(HttpServletResponse.SC_NOT_FOUND,
                HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Loggable
    public ErrorResponse handleBeanValidation(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuffer errorMessage = new StringBuffer();
        for(FieldError field : fieldErrors) {
            errorMessage.append(field.getField())
                .append(": ")
                .append(field.getDefaultMessage())
                .append(", ");
        }
        errorMessage.setCharAt(errorMessage.length()-2, '.');
        return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                HttpStatus.BAD_REQUEST, errorMessage.toString().trim());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @Loggable
    public ErrorResponse invalidInput(HttpMessageNotReadableException e) {
        StringBuffer errorMessage = new StringBuffer()
                .append("exception: ")
                .append(e);
        return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                HttpStatus.BAD_REQUEST, errorMessage.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    @Loggable
    public ErrorResponse defaultErrorHandler(HttpServletRequest req, Exception e) {
        StringBuffer errorMessage = new StringBuffer()
                .append("exception: ")
                .append(e);
        return new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR, errorMessage.toString());
    }
}
