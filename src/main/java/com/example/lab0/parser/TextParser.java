package com.example.lab0.parser;

import java.util.List;

/**
 * Интерфейс для парсинга текста и извлечения слов.
 * Позволяет использовать различные стратегии токенизации.
 */
@FunctionalInterface
public interface TextParser {

    /**
     * Извлекает слова из текстового содержимого.
     *
     * @param content текстовое содержимое
     * @return список слов
     */
    List<String> parse(String content);
}
