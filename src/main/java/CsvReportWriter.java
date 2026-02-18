import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Класс для записи статистики в CSV-формат.
 */
public class CsvReportWriter {

    private static final String CSV_HEADER = "Слово,Частота,Частота (%)";
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Записывает статистику в CSV-файл.
     *
     * @param fileName имя выходного файла
     * @param stats    список статистики по словам
     * @throws IOException если не удалось записать файл
     */
    public void write(String fileName, List<WordStat> stats) throws IOException {
        Path path = Paths.get(fileName);
        Path parentDir = path.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }

        try (Writer writer = Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8)) {
            writer.write(CSV_HEADER);
            writer.write(LINE_SEPARATOR);

            for (WordStat stat : stats) {
                writer.write(escapeCsvField(stat.word()));
                writer.write(",");
                writer.write(Integer.toString(stat.count()));
                writer.write(",");
                writer.write(String.format(java.util.Locale.US, "%.4f", stat.percentage()));
                writer.write(LINE_SEPARATOR);
            }
        }
    }

    /**
     * Экранирует поле для CSV: если поле содержит запятую, кавычку или перенос строки,
     * оно заключается в кавычки, а внутренние кавычки удваиваются.
     */
    private String escapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return "";
        }
        boolean needsQuotes = field.contains(",") || field.contains("\"") || 
                              field.contains("\n") || field.contains("\r");
        
        if (!needsQuotes) {
            return field;
        }
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }
}