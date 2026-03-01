package com.example.lab0.writer;

import com.example.lab0.analyzer.WordStat;
import com.example.lab0.constants.AppConstants;
import com.example.lab0.io.ReportWriter;
import com.example.lab0.validation.FileValidator;
import com.example.lab0.validation.GenericValidator;
import com.example.lab0.validation.PathValidator;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Реализация ReportWriter для записи статистики в CSV-файл.
 * Потокобезопасен и неизменяем.
 */
public class CsvReportWriter implements ReportWriter<WordStat> {

    private static final Locale CSV_LOCALE = Locale.US;

    @Override
    public void write(Path path, List<WordStat> stats) throws IOException {
        validateWriteParameters(path, stats);
        FileValidator.validateExists(path.getParent());

        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writeHeader(writer);
            writeData(writer, stats);
        }
    }

    private void validateWriteParameters(Path path, List<WordStat> stats) {
        PathValidator.validate(path);
        GenericValidator.requireNoNullElements(stats, "Список статистики");
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.write(AppConstants.CSV_HEADER);
        writer.write(System.lineSeparator());
    }

    private void writeData(Writer writer, List<WordStat> stats) throws IOException {
        for (WordStat stat : stats) {
            Objects.requireNonNull(stat, "Элемент статистики не может быть null");
            writeCsvRow(writer, stat);
        }
    }

    private void writeCsvRow(Writer writer, WordStat stat) throws IOException {
        writer.write(escapeCsvField(stat.word()));
        writer.write(String.valueOf(AppConstants.COMMA));
        writer.write(Integer.toString(stat.count()));
        writer.write(String.valueOf(AppConstants.COMMA));
        writer.write(String.format(CSV_LOCALE, AppConstants.CSV_PERCENTAGE_FORMAT, stat.fraction() * 100.0));
        writer.write(System.lineSeparator());
    }

    /**
     * Экранирует поле для CSV согласно RFC 4180.
     */
    private String escapeCsvField(String field) {
        if (field == null || field.isEmpty()) {
            return "";
        }

        boolean needsQuotes = field.contains(String.valueOf(AppConstants.COMMA))
                || field.contains(String.valueOf(AppConstants.DOUBLE_QUOTE))
                || field.contains(String.valueOf(AppConstants.NEWLINE))
                || field.contains(String.valueOf(AppConstants.CARRIAGE_RETURN));

        if (!needsQuotes) {
            return field;
        }

        return AppConstants.DOUBLE_QUOTE + field.replace(
                String.valueOf(AppConstants.DOUBLE_QUOTE),
                String.valueOf(AppConstants.DOUBLE_QUOTE) + String.valueOf(AppConstants.DOUBLE_QUOTE)
        ) + AppConstants.DOUBLE_QUOTE;
    }
}
