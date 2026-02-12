import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Класс, отвечающий за экспорт данных
class CsvReportWriter {
    public void write(String fileName, List<WordStat> stats) throws IOException {
        try (PrintWriter writer = new PrintWriter(fileName, StandardCharsets.UTF_8)) {
            writer.println("Слово,Частота,Частота (%)");
            for (WordStat stat : stats) {
                writer.println(stat.toCsvRow());
            }
        }
    }
}