package com.example.lab0.validation;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.exception.InvalidInputException;

import java.util.List;
import java.util.Objects;

/**
 * Универсальный валидатор для различных типов данных.
 */
public final class GenericValidator {

    private GenericValidator() {
        throw new UnsupportedOperationException("Класс валидатора не может быть инстанцирован");
    }

    /**
     * Валидирует, что значение не null.
     *
     * @param value       значение
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если значение null
     */
    public static <T> T requireNotNull(T value, String fieldName) {
        return Objects.requireNonNull(value, fieldName + " не может быть null");
    }

    /**
     * Валидирует, что число неотрицательное.
     *
     * @param value       значение
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если значение отрицательное
     */
    public static int requireNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new InvalidInputException(fieldName + " не может быть отрицательным: " + value);
        }
        return value;
    }

    /**
     * Валидирует, что число положительное.
     *
     * @param value       значение
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если значение не положительное
     */
    public static long requirePositive(long value, String fieldName) {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " должно быть положительным: " + value);
        }
        return value;
    }

    /**
     * Валидирует, что значение в диапазоне [min, max].
     *
     * @param value       значение
     * @param min         минимальное значение (включительно)
     * @param max         максимальное значение (включительно)
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если значение вне диапазона
     */
    public static double requireInRange(double value, double min, double max, String fieldName) {
        if (value < min || value > max) {
            throw new InvalidInputException(
                String.format("%s должно быть в диапазоне [%.2f, %.2f]: %.2f", fieldName, min, max, value)
            );
        }
        return value;
    }

    /**
     * Валидирует, что список не null и не содержит null элементов.
     *
     * @param list        список
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если список null или содержит null элементы
     */
    public static <T> List<T> requireNoNullElements(List<T> list, String fieldName) {
        Objects.requireNonNull(list, fieldName + " не может быть null");
        for (T item : list) {
            Objects.requireNonNull(item, fieldName + " не может содержать null элементы");
        }
        return list;
    }

    /**
     * Валидирует длину строки.
     *
     * @param value       строка
     * @param maxLength   максимальная длина
     * @param fieldName   имя поля для сообщения об ошибке
     * @throws InvalidInputException если строка слишком длинная
     */
    public static String validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new InvalidInputException(
                String.format("%s слишком длинный (максимум %d символов)", fieldName, maxLength)
            );
        }
        return value;
    }

    /**
     * Валидирует, что слово не пустое и не превышает максимальную длину.
     *
     * @param word слово
     * @return нормализованное слово (trimmed)
     * @throws InvalidInputException если слово невалидно
     */
    public static String validateWord(String word) {
        Objects.requireNonNull(word, "Слово не может быть null");

        String trimmed = word.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidInputException("Слово не может быть пустым");
        }

        if (trimmed.length() > AppConstants.MAX_WORD_LENGTH) {
            throw new InvalidInputException(
                String.format("Слово слишком длинное (максимум %d символов)", AppConstants.MAX_WORD_LENGTH)
            );
        }

        return trimmed;
    }
}
