package com.example.lab0.analyzer;

import com.example.lab0.exception.InvalidInputException;

import java.util.Locale;
import java.util.Objects;

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
    private static final String CSV_FORMAT = "%s,%d,%.4f";

    /**
     * Основной конструктор с полной валидацией параметров.
     */
    public WordStat {
        Objects.requireNonNull(word, "word не может быть null");

        String trimmedWord = word.trim();
        if (trimmedWord.isEmpty()) {
            throw new IllegalArgumentException("word не может быть пустым");
        }

        if (trimmedWord.length() > 500) {
            throw new IllegalArgumentException("word слишком длинный (максимум 500 символов)");
        }

        if (count < 0) {
            throw new IllegalArgumentException("count не может быть отрицательным: " + count);
        }

        if (percentage < 0.0 || percentage > 100.0) {
            throw new IllegalArgumentException(
                "percentage должно быть в диапазоне [0, 100]: " + percentage
            );
        }

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
        this(word, count, totalWords > 0 ? (count * 100.0 / totalWords) : 0.0);

        if (totalWords <= 0) {
            throw new IllegalArgumentException("totalWords должно быть положительным: " + totalWords);
        }
    }

    /**
     * Форматирует статистику как CSV-строку.
     */
    public String toCsvRow() {
        return String.format(CSV_LOCALE, CSV_FORMAT, word, count, percentage);
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
