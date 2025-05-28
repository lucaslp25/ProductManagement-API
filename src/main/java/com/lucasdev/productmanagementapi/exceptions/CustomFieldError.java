package com.lucasdev.productmanagementapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CustomFieldError implements Serializable {
    private static final long serialVersionUID = 1L;

    String fieldName; //name of field that fail
    String message; // message for that field
}

//this class go´s be util for the MethodArgumentNotValidException error, when our need put in a list all the errors, that is causing error in requisition