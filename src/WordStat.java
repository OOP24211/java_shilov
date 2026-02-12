import java.util.Locale;

// DTO
class WordStat {
    private final String word;
    private final int count;
    private final double percentage;

    public WordStat(String word, int count, long totalWords) {
        this.word = word;
        this.count = count;
        this.percentage = (totalWords > 0) ? (count * 100.0 / totalWords) : 0;
    }

    public String toCsvRow() {
        // Используем точку как разделитель дробной части для стандарта CSV
        return String.format(Locale.US, "%s,%d,%.4f", word, count, percentage);
    }
}
