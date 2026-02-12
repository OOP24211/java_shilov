import java.io.IOException;
import java.util.*;

// Entry Point
public class WordFrequencyApp {
    public static void main(String[] args) {

        String inputPath = "test.txt";
        String outputPath = inputPath + ".csv";

        try {
            TextAnalyzer analyzer = new TextAnalyzer(inputPath);
            List<WordStat> stats = analyzer.analyze();

            if (stats.isEmpty()) {
                System.out.println("Файл пуст или не содержит слов.");
                return;
            }

            CsvReportWriter reportWriter = new CsvReportWriter();
            reportWriter.write(outputPath, stats);

            System.out.println("CSV файл создан: " + outputPath);

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}