
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Точка входа в приложение для анализа текста и генерации CSV-отчета.
 */
@Slf4j
public class WordFrequencyApp {

    public static void main(String[] args) {
        String inputPath = "test.txt";

        try {
            String outputPath = buildOutputPath(inputPath);

            TextAnalyzer analyzer = new TextAnalyzer(inputPath);
            var stats = analyzer.analyze();

            if (stats.isEmpty()) {
                System.out.println("Файл пуст или не содержит слов.");
                return;
            }

            CsvReportWriter writer = new CsvReportWriter();
            writer.write(outputPath, stats);

            long wordCount = stats.stream().mapToInt(WordStat::count).sum();
            System.out.printf("Обработано слов: %d%n", wordCount);
            System.out.printf("Уникальных слов: %d%n", stats.size());
            System.out.printf("CSV файл создан: %s%n", outputPath);

        } catch (IOException e) {
            log.error("Ошибка при работе с файлом: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Произошла непредвиденная ошибка: {}", e.getMessage());
        }
    }

    private static String buildOutputPath(String inputPath) {
        Path path = Paths.get(inputPath);
        String fileName = path.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        String nameWithoutExt = (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
        return nameWithoutExt + ".csv";
    }
}