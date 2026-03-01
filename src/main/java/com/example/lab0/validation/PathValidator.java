package com.example.lab0.validation;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.exception.InvalidInputException;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Валидатор путей к файлам.
 * Инкапсулирует всю логику проверки путей в одном месте.
 */
public final class PathValidator {

    private PathValidator() {
        throw new UnsupportedOperationException("Класс валидатора не может быть инстанцирован");
    }

    /**
     * Валидирует строковый путь и возвращает нормализованный Path.
     *
     * @param filePath строковый путь
     * @return валидный Path объект
     * @throws InvalidInputException если путь невалиден
     */
    public static Path validate(String filePath) {
        validateNotNull(filePath, "Путь к файлу");
        validateNotEmpty(filePath, "Путь к файлу");
        validateNoNullCharacters(filePath, "Путь к файлу");
        validateLength(filePath, AppConstants.MAX_PATH_LENGTH, "Путь к файлу");

        return java.nio.file.Paths.get(filePath.trim());
    }

    /**
     * Валидирует Path объект.
     *
     * @param path Path объект
     * @return валидный Path объект
     * @throws InvalidInputException если путь null
     */
    public static Path validate(Path path) {
        return Objects.requireNonNull(path, "Путь не может быть null");
    }

    /**
     * Валидирует имя файла.
     *
     * @param fileName имя файла
     * @throws InvalidInputException если имя невалидно
     */
    public static void validateFileName(String fileName) {
        validateNotNull(fileName, "Имя файла");
        validateNotEmpty(fileName, "Имя файла");
        validateNoNullCharacters(fileName, "Имя файла");
        validateLength(fileName, AppConstants.MAX_FILENAME_LENGTH, "Имя файла");
    }

    private static void validateNotNull(String value, String fieldName) {
        if (value == null) {
            throw new InvalidInputException(fieldName + " не может быть null");
        }
    }

    private static void validateNotEmpty(String value, String fieldName) {
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidInputException(fieldName + " не может быть пустым");
        }
    }

    private static void validateNoNullCharacters(String value, String fieldName) {
        if (value.contains(String.valueOf(AppConstants.NULL_CHARACTER))) {
            throw new InvalidInputException(fieldName + " содержит недопустимые символы");
        }
    }

    private static void validateLength(String value, int maxLength, String fieldName) {
        String trimmed = value.trim();
        if (trimmed.length() > maxLength) {
            throw new InvalidInputException(
                String.format("%s слишком длинный (максимум %d символов)", fieldName, maxLength)
            );
        }
    }
}
