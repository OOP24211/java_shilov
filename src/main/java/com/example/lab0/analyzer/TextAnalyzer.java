package com.example.lab0.analyzer;

import com.example.lab0.exception.FileOperationException;
import com.example.lab0.exception.InvalidInputException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Анализатор текстовых файлов для подсчета частоты слов.
 * Потокобезопасен и неизменяем после создания.
 */
public class TextAnalyzer {

    private static final Pattern WORD_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]+");
    private static final int MAX_FILE_SIZE_BYTES = 100 * 1024 * 1024; // 100 MB

    private final Path filePath;

    /**
     * Создает анализатор с валидацией пути.
     *
     * @param filePath путь к файлу
     * @throws InvalidInputException если путь невалиден
     */
    public TextAnalyzer(String filePath) {
        this.filePath = validateAndParsePath(filePath);
    }

    /**
     * Создает анализатор с валидацией Path объекта.
     *
     * @param filePath путь к файлу
     * @throws InvalidInputException если путь невалиден
     */
    public TextAnalyzer(Path filePath) {
        this.filePath = validatePath(filePath);
    }

    private static Path validateAndParsePath(String filePath) {
        if (filePath == null) {
            throw new InvalidInputException("Путь к файлу не может быть null");
        }

        String trimmed = filePath.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidInputException("Путь к файлу не может быть пустым");
        }

        if (trimmed.contains("\0")) {
            throw new InvalidInputException("Путь содержит недопустимые символы");
        }

        return Paths.get(trimmed);
    }

    private static Path validatePath(Path filePath) {
        return Objects.requireNonNull(filePath, "filePath не может быть null");
    }

    /**
     * Анализирует текстовый файл и возвращает статистику по словам.
     *
     * @return список объектов WordStat, отсортированных по убыванию частоты
     * @throws IOException если файл не может быть прочитан
     * @throws FileOperationException если файл не существует или имеет недопустимый размер
     */
    public List<WordStat> analyze() throws IOException {
        validateFileBeforeRead();

        long fileSize = Files.size(filePath);
        if (fileSize > MAX_FILE_SIZE_BYTES) {
            throw new FileOperationException(
                String.format("Файл слишком большой (%.2f MB). Максимум: %d MB",
                    fileSize / (1024.0 * 1024.0), MAX_FILE_SIZE_BYTES / (1024 * 1024))
            );
        }

        String content = Files.readString(filePath, StandardCharsets.UTF_8);

        if (content.isBlank()) {
            return List.of();
        }

        List<String> words = extractWords(content);
        if (words.isEmpty()) {
            return List.of();
        }

        long totalWords = words.size();
        Map<String, Long> frequencyMap = calculateFrequency(words);

        return buildResult(frequencyMap, totalWords);
    }

    private void validateFileBeforeRead() throws IOException {
        if (!Files.exists(filePath)) {
            throw new FileOperationException("Файл не найден: " + filePath);
        }

        if (!Files.isReadable(filePath)) {
            throw new FileOperationException("Файл недоступен для чтения: " + filePath);
        }

        if (Files.isDirectory(filePath)) {
            throw new FileOperationException("Путь указывает на директорию, а не файл: " + filePath);
        }
    }

    private List<String> extractWords(String content) {
        return WORD_PATTERN.splitAsStream(content.toLowerCase())
                .filter(word -> !word.isBlank())
                .toList();
    }

    private Map<String, Long> calculateFrequency(List<String> words) {
        return words.stream()
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()
                ));
    }

    private List<WordStat> buildResult(Map<String, Long> frequencyMap, long totalWords) {
        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new WordStat(entry.getKey(), entry.getValue().intValue(), totalWords))
                .toList();
    }

    /**
     * Возвращает путь к анализируемому файлу.
     */
    public Path getFilePath() {
        return filePath;
    }
}
