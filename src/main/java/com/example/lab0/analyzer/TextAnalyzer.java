package com.example.lab0.analyzer;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.frequency.FrequencyCalculator;
import com.example.lab0.frequency.StandardFrequencyCalculator;
import com.example.lab0.io.FileReader;
import com.example.lab0.io.StandardFileReader;
import com.example.lab0.model.TextAnalysisResult;
import com.example.lab0.parser.TextParser;
import com.example.lab0.parser.StandardTextParser;
import com.example.lab0.validation.FileValidator;
import com.example.lab0.validation.PathValidator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Анализатор текстовых файлов для подсчета частоты слов.
 * Потокобезопасен и неизменяем после создания.
 * Использует внедрение зависимостей через конструктор для гибкости и тестируемости.
 */
public class TextAnalyzer {

    private final Path filePath;
    private final FileReader fileReader;
    private final TextParser textParser;
    private final FrequencyCalculator<String> frequencyCalculator;

    /**
     * Создает анализатор с валидацией пути и зависимостями по умолчанию.
     *
     * @param filePath путь к файлу
     */
    public TextAnalyzer(String filePath) {
        this(
            PathValidator.validate(filePath),
            new StandardFileReader(),
            new StandardTextParser(),
            new StandardFrequencyCalculator()
        );
    }

    /**
     * Создает анализатор с валидацией Path объекта и зависимостями по умолчанию.
     *
     * @param filePath путь к файлу
     */
    public TextAnalyzer(Path filePath) {
        this(
            PathValidator.validate(filePath),
            new StandardFileReader(),
            new StandardTextParser(),
            new StandardFrequencyCalculator()
        );
    }

    /**
     * Создает анализатор с внедренными зависимостями.
     * Позволяет использовать кастомные реализации для тестирования или расширения функциональности.
     *
     * @param filePath            путь к файлу
     * @param fileReader          читатель файлов
     * @param textParser          парсер текста
     * @param frequencyCalculator калькулятор частоты
     */
    public TextAnalyzer(
            Path filePath,
            FileReader fileReader,
            TextParser textParser,
            FrequencyCalculator<String> frequencyCalculator
    ) {
        this.filePath = PathValidator.validate(filePath);
        this.fileReader = fileReader;
        this.textParser = textParser;
        this.frequencyCalculator = frequencyCalculator;
    }

    /**
     * Анализирует текстовый файл и возвращает результат анализа.
     *
     * @return результат анализа текста
     * @throws IOException если файл не может быть прочитан
     */
    public TextAnalysisResult analyze() throws IOException {
        validateFileBeforeRead();

        String content = fileReader.read(filePath);
        return processContent(content);
    }

    /**
     * Анализирует текстовое содержимое напрямую (без чтения из файла).
     *
     * @param content текстовое содержимое
     * @return результат анализа текста
     */
    public TextAnalysisResult analyzeContent(String content) {
        return processContent(content);
    }

    private void validateFileBeforeRead() {
        FileValidator.validateReadableFile(filePath);
        FileValidator.validateFileSize(filePath, AppConstants.MAX_FILE_SIZE_BYTES);
    }

    private TextAnalysisResult processContent(String content) {
        if (content == null || content.isBlank()) {
            return new TextAnalysisResult(List.of());
        }

        var words = textParser.parse(content);
        if (words.isEmpty()) {
            return new TextAnalysisResult(List.of());
        }

        Map<String, Long> frequencyMap = frequencyCalculator.calculate(words);
        long totalWords = words.size();

        var stats = frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new WordStat(entry.getKey(), entry.getValue().intValue(), totalWords))
                .toList();

        return new TextAnalysisResult(stats);
    }

    /**
     * Возвращает путь к анализируемому файлу.
     */
    public Path getFilePath() {
        return filePath;
    }
}
