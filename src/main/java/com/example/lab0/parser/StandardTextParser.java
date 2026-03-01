package com.example.lab0.parser;

import com.example.lab0.constants.AppConstants;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Стандартный парсер текста, разделяющий текст на слова по не-алфанумерическим символам.
 * Поддерживает Unicode буквы и цифры.
 */
public class StandardTextParser implements TextParser {

    private static final Pattern WORD_DELIMITER_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]+");

    @Override
    public List<String> parse(String content) {
        if (content == null || content.isBlank()) {
            return List.of();
        }

        return Arrays.stream(WORD_DELIMITER_PATTERN.split(content.toLowerCase()))
                .filter(word -> !word.isBlank())
                .toList();
    }
}
