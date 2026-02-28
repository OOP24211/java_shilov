package com.example.lab0.analyzer;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.validation.GenericValidator;

import java.util.Locale;

/**
 * DTO для хранения статистики по слову.
 * Использует record для компактной и неизменяемой структуры данных.
 *
 * @param word       слово (нормализованное, в нижнем регистре)
 * @param count      количество вхождений
 * @param percentage процент от общего числа слов
 */
public record WordStat(String word, int count, double percentage) {

    private static final Locale CSV_LOCALE = Locale.US;

    /**
     * Основной конструктор с полной валидацией параметров.
     */
    public WordStat {
        String trimmedWord = GenericValidator.validateWord(word);
        GenericValidator.requireNonNegative(count, "count");
        GenericValidator.requireInRange(percentage, 0.0, 100.0, "percentage");

        word = trimmedWord;
    }

    /**
     * Компактный конструктор для вычисления percentage на основе totalWords.
     *
     * @param word       слово
     * @param count      количество вхождений (должно быть >= 0)
     * @param totalWords общее число слов в тексте (должно быть > 0)
     * @throws IllegalArgumentException если count < 0 или totalWords <= 0
     */
    public WordStat(String word, int count, long totalWords) {
        this(word, count, GenericValidator.requirePositive(totalWords, "totalWords") > 0
                ? (count * 100.0 / totalWords)
                : 0.0);
    }

    /**
     * Форматирует статистику как CSV-строку.
     */
    public String toCsvRow() {
        return String.format(CSV_LOCALE, AppConstants.CSV_ROW_FORMAT, word, count, percentage);
    }

    @Override
    public String toString() {
        return String.format(
            CSV_LOCALE,
            "WordStat{word='%s', count=%d, percentage=%.2f%%}",
            word, count, percentage
        );
    }
}
