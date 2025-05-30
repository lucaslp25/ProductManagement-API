package com.lucasdev.productmanagementapi.exceptions;

public class IntegrityDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public IntegrityDataException(String message) {
        super(message);
    }
}
