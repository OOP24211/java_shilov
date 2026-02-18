import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Класс для анализа текстовых файлов и подсчета частоты слов.
 */
public class TextAnalyzer {

    private static final Pattern WORD_PATTERN = Pattern.compile("[^\\p{L}\\p{N}]+");

    private final Path filePath;

    public TextAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public TextAnalyzer(Path filePath) {
        this.filePath = Objects.requireNonNull(filePath, "filePath cannot be null");
    }

    /**
     * Анализирует текстовый файл и возвращает статистику по словам.
     * 
     * @return список объектов WordStat, отсортированных по убыванию частоты
     * @throws IOException если файл не найден или не может быть прочитан
     */
    public List<WordStat> analyze() throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("Файл не найден: " + filePath);
        }

        String content = Files.readString(filePath, StandardCharsets.UTF_8).toLowerCase();

        List<String> words = WORD_PATTERN.splitAsStream(content)
                .filter(word -> !word.isBlank())
                .toList();

        long totalWords = words.size();
        if (totalWords == 0) {
            return List.of();
        }

        Map<String, Long> frequencyMap = words.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        word -> word,
                        java.util.stream.Collectors.counting()
                ));

        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> new WordStat(entry.getKey(), entry.getValue().intValue(), totalWords))
                .toList();
    }
}