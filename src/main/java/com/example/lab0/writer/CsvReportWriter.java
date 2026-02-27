package com.example.lab0.writer;

import com.example.lab0.analyzer.WordStat;
import com.example.lab0.exception.InvalidInputException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Записчик статистики слов в CSV-файл.
 * Потокобезопасен и неизменяем.
 */
public class CsvReportWriter {

    private static final String CSV_HEADER = "Слово,Частота,Частота (%)";
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final Locale CSV_LOCALE = Locale.US;

    /**
     * Записывает статистику в CSV-файл.
     *
     * @param fileName имя выходного файла
     * @param stats    список статистики по словам
     * @throws IOException          если не удалось записать файл
     * @throws InvalidInputException если параметры невалидны
     */
    public void write(String fileName, List<WordStat> stats) throws IOException {
        validateWriteParameters(fileName, stats);

        Path path = Paths.get(fileName).toAbsolutePath().normalize();
        ensureParentDirectoryExists(path);

        try (Writer writer = Files.newBufferedWriter(path, java.nio.charset.StandardCharsets.UTF_8)) {
            writeHeader(writer);
            writeData(writer, stats);
        }
    }

    private void validateWriteParameters(String fileName, List<WordStat> stats) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new InvalidInputException("Имя файла не может быть пустым");
        }

        String trimmedFileName = fileName.trim();
        if (trimmedFileName.length() > 500) {
            throw new InvalidInputException("Имя файла слишком длинное (максимум 500 символов)");
        }

        if (trimmedFileName.contains("\0")) {
            throw new InvalidInputException("Имя файла содержит недопустимые символы");
        }

        if (stats == null) {
            throw new InvalidInputException("Список статистики не может быть null");
        }
    }

    private void ensureParentDirectoryExists(Path path) throws IOException {
        Path parentDir = path.getParent();
        if (parentDir != null && !Files.exists(parentDir)) {
            Files.createDirectories(parentDir);
        }
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.write(CSV_HEADER);
        writer.write(LINE_SEPARATOR);
    }

    private void writeData(Writer writer, List<WordStat> stats) throws IOException {
        for (WordStat stat : stats) {
            Objects.requireNonNull(stat, "Элемент статистики не может быть null");
            writeCsvRow(writer, stat);
        }
    }

    private void writeCsvRow(Writer writer, WordStat stat) throws IOException {
        writer.write(escapeCsvField(stat.word()));
        writer.write(",");
        writer.write(Integer.toString(stat.count()));
        writer.write(",");
        writer.write(String.format(CSV_LOCALE, "%.4f", stat.percentage()));
        writer.write(LINE_SEPARATOR);
    }

    /**
     * Экранирует поле для CSV согласно RFC 4180.
     * Если поле содержит запятую, кавычку или перенос строки,
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
