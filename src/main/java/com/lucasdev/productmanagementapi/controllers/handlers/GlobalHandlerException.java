package com.lucasdev.productmanagementapi.controllers.handlers;

import com.lucasdev.productmanagementapi.exceptions.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.time.Instant;
import java.util.InputMismatchException;

@ControllerAdvice
public class GlobalHandlerException { //this class controls all my exceptions! it´s a good programing practice!

    //first i'm configure the default exceptions of program, after i'll putting my personalized exceptions that i'll creating

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError();

        //here i customize my complete error message

        error.setTimestamp(Instant.now());  //take this moment(now)
        error.setStatus(status);    //the error status... 404 is for not found!
        error.setError("Resource not found"); // default message for my exception
        error.setMessage(ex.getMessage()); // this message i´ll define in hour that i throw the exception
        error.setPath(request.getRequestURI()); // get the uri path

        //return the exception with the custom message
        return ResponseEntity.status(status).body(error);
    }

    //treat the invalid enters, and integrity errors!
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String error = "Data Integrity Violation";
        String message = "A data integrity constraint was violated. Please check your input";

        String sqlState = null;
        Throwable currentCause = ex;
        //while for find the cause
        while (currentCause != null) {
            if (currentCause instanceof SQLException) {
                sqlState = ((SQLException) currentCause).getSQLState();
                break;
            }
            currentCause = currentCause.getCause();
        }
        // use the SQLSTATE for personalize the message of IntegrityViolation
        if (sqlState != null) {
            if (sqlState.equals("23505")) { // SQLSTATE for Unique Constraint Violation

                message = "The entered value for a unique field already exists.";
                error = "Unique Constraint Violation";

            } else if (sqlState.equals("23503")) { // SQLSTATE for Foreign Key Violation
                message = "Cannot delete or update this resource as it is linked to other existing data.";
                error = "Foreign Key Violation";

            } else if (sqlState.startsWith("23")) {

                message = "A database integrity constraint was violated.";
                error = "Database Constraint Error";
            }
        }
        //if SQLSTATE not found, go´s keep the method initial message
        StandardError err = new StandardError(Instant.now(), status, error, message, request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    //treating the error of invalid arguments
    //is very important understand that this method treat the enters of users, in the controller layer, i do not use this on service layer because this don´t arrive in that layer
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        String message = "One or more fields failed validation.";
        String error = "Validation error";

        ValidationError err = new ValidationError(Instant.now(), status, error, message, request.getRequestURI());

        //iterating over the FieldError for take the fields, and put in the list
        for (FieldError errorField : ex.getBindingResult().getFieldErrors()){
            //put the name of error, and the message of error!!
            err.addFieldError(errorField.getField(), errorField.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }

    //custom exception if service Unavailable
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<StandardError> serviceUnavailable(ServiceUnavailableException ex, HttpServletRequest request) {

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Service Unavailable");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    // custom exception for invalid number enters
    @ExceptionHandler(InputMismatchException.class)
    public ResponseEntity<StandardError> inputMismatch(InputMismatchException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Input Mismatch");
        error.setMessage("Only numbers allowed"); //my default message for this exception
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(status).body(error);
    }

    //exception for database errors formats enters
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<StandardError> dataBaseException(DataBaseException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Database Error");
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    //a generic error (it´s interesting) with a error code 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> exception(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError error = new StandardError(); //code 500 in this situation
        error.setTimestamp(Instant.now());
        error.setStatus(status);
        error.setError("Internal Server Error");
        error.setMessage("A Unexpected error occurred"); //can be a default message here
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }
}