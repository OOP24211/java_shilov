package com.example.lab0.exception;

/**
 * Исключение для ошибок невалидных входных данных.
 */
public class InvalidInputException extends TextAnalysisException {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
