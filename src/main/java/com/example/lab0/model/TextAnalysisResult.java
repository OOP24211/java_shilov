package com.example.lab0.model;

import com.example.lab0.analyzer.WordStat;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Результат анализа текста.
 * Инкапсулирует всю статистику по анализу в одном объекте.
 */
public final class TextAnalysisResult {

    private final List<WordStat> wordStats;
    private final long totalWords;
    private final int uniqueWords;

    /**
     * Создает результат анализа.
     *
     * @param wordStats статистика по каждому слову
     */
    public TextAnalysisResult(List<WordStat> wordStats) {
        this.wordStats = wordStats != null ? List.copyOf(wordStats) : List.of();
        this.uniqueWords = this.wordStats.size();
        this.totalWords = this.wordStats.stream()
                .mapToInt(WordStat::count)
                .sum();
    }

    /**
     * Возвращает статистику по словам.
     *
     * @return неизменяемый список статистики
     */
    public List<WordStat> getWordStats() {
        return wordStats;
    }

    /**
     * Возвращает общее количество слов в тексте.
     *
     * @return общее количество слов
     */
    public long getTotalWords() {
        return totalWords;
    }

    /**
     * Возвращает количество уникальных слов.
     *
     * @return количество уникальных слов
     */
    public int getUniqueWords() {
        return uniqueWords;
    }

    /**
     * Проверяет, пуст ли результат анализа.
     *
     * @return true если нет слов для анализа
     */
    public boolean isEmpty() {
        return wordStats.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextAnalysisResult that = (TextAnalysisResult) o;
        return totalWords == that.totalWords && uniqueWords == that.uniqueWords
                && Objects.equals(wordStats, that.wordStats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordStats, totalWords, uniqueWords);
    }

    @Override
    public String toString() {
        return String.format(
            "TextAnalysisResult{totalWords=%d, uniqueWords=%d, stats=%s}",
            totalWords, uniqueWords, wordStats.size()
        );
    }
}
