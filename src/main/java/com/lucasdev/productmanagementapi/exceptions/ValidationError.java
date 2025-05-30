package com.lucasdev.productmanagementapi.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationError extends StandardError{

    //this list will contains of the details error exception
    private List<CustomFieldError> fieldErrors = new ArrayList<>();

    //take the atribbuts of the superclass StandardError
    public ValidationError(Instant timestamp, HttpStatus status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    //method for put in the list an error with the customized message!
    public void addFieldError(String fieldName, String message) {
        //new instance of CustomFieldError here
        fieldErrors.add(new CustomFieldError(fieldName, message));
    }
}
