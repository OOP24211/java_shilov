import java.util.Locale;

/**
 * DTO для хранения статистики по слову.
 * Использует record для компактной и неизменяемой структуры данных.
 */
public record WordStat(String word, int count, double percentage) {

    public WordStat(String word, int count, long totalWords) {
        this(word, count, totalWords > 0 ? (count * 100.0 / totalWords) : 0.0);
    }

    public String toCsvRow() {
        return String.format(Locale.US, "%s,%d,%.4f", word, count, percentage);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "WordStat{word='%s', count=%d, percentage=%.2f%%}", word, count, percentage);
    }
}