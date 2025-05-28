package com.lucasdev.productmanagementapi.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class StandardError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp; //hour of error
    private HttpStatus status; // HTTP status code
    private String error; // name of error
    private String message; // the message of error
    private String path; // the path of the error
}
//this class will personalize the errors in the final user console! With the attributs this.