package com.example.lab0.analyzer;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.validation.GenericValidator;

import java.util.Locale;

/**
 * DTO для хранения статистики по слову.
 * Использует record для компактной и неизменяемой структуры данных.
 *
 * @param word     слово (нормализованное, в нижнем регистре)
 * @param count    количество вхождений
 * @param fraction доля от общего числа слов (0.0–1.0)
 */
public record WordStat(String word, int count, double fraction) {

    private static final Locale CSV_LOCALE = Locale.US;

    /**
     * Основной конструктор с полной валидацией параметров.
     */
    public WordStat {
        String trimmedWord = GenericValidator.validateWord(word);
        GenericValidator.requireNonNegative(count, "count");
        GenericValidator.requireInRange(fraction, 0.0, 1.0, "fraction");

        word = trimmedWord;
    }

    /**
     * Компактный конструктор для вычисления fraction на основе totalWords.
     *
     * @param word       слово
     * @param count      количество вхождений (должно быть >= 0)
     * @param totalWords общее число слов в тексте (должно быть > 0)
     * @throws IllegalArgumentException если count < 0 или totalWords <= 0
     */
    public WordStat(String word, int count, long totalWords) {
        this(word, count, GenericValidator.requirePositive(totalWords, "totalWords") > 0
                ? (count / (double) totalWords)
                : 0.0);
    }

    /**
     * Форматирует статистику как CSV-строку.
     */
    public String toCsvRow() {
        return String.format(CSV_LOCALE, AppConstants.CSV_ROW_FORMAT, word, count, fraction * 100.0);
    }

    @Override
    public String toString() {
        return String.format(
            CSV_LOCALE,
            "WordStat{word='%s', count=%d, fraction=%.4f (%.2f%%)}",
            word, count, fraction, fraction * 100.0
        );
    }
}
