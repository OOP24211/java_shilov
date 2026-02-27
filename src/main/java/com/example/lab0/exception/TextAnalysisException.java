package com.example.lab0.exception;

/**
 * Базовое исключение для ошибок анализа текста.
 */
public class TextAnalysisException extends RuntimeException {

    public TextAnalysisException(String message) {
        super(message);
    }

    public TextAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
