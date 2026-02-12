import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// Класс, инкапсулирующий логику чтения и анализа текста
class TextAnalyzer {
    private final Path filePath;

    public TextAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public List<WordStat> analyze() throws IOException {
        String content = Files.readString(filePath, StandardCharsets.UTF_8).toLowerCase();

        // Поддержка Unicode
        Pattern delimiter = Pattern.compile("[^\\p{L}\\p{N}]+");

        // Разбиение на слова
        List<String> words = delimiter.splitAsStream(content)
                .filter(w -> !w.isBlank())
                .collect(Collectors.toList());

        long totalWords = words.size();
        if (totalWords == 0) return Collections.emptyList();

        // Подсчет частоты
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.merge(word, 1, Integer::sum);
        }

        // Преобразование в объекты WordStat и сортировка
        return frequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(entry -> new WordStat(entry.getKey(), entry.getValue(), totalWords))
                .collect(Collectors.toList());
    }
}