package com.example.lab0.exception;

/**
 * Исключение для ошибок операций с файлами.
 */
public class FileOperationException extends TextAnalysisException {

    public FileOperationException(String message) {
        super(message);
    }

    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
