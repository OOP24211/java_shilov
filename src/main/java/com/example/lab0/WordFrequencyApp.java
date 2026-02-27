package com.example.lab0;

import com.example.lab0.analyzer.TextAnalyzer;
import com.example.lab0.analyzer.WordStat;
import com.example.lab0.config.AppConfiguration;
import com.example.lab0.exception.FileOperationException;
import com.example.lab0.exception.InvalidInputException;
import com.example.lab0.writer.CsvReportWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Точка входа в приложение для анализа текста и генерации CSV-отчета.
 * Реализует полную валидацию входных данных и обработку ошибок.
 */
@Slf4j
public class WordFrequencyApp {

    private static final int EXIT_CODE_SUCCESS = 0;
    private static final int EXIT_CODE_INVALID_ARGS = 1;
    private static final int EXIT_CODE_FILE_ERROR = 2;
    private static final int EXIT_CODE_PROCESSING_ERROR = 3;

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки:
     *             - 0 аргументов: используется файл по умолчанию (test.txt)
     *             - 1 аргумент: путь к входному файлу
     *             - >1 аргументов: ошибка
     */
    public static void main(String[] args) {
        int exitCode = runApplication(args);
        if (exitCode != EXIT_CODE_SUCCESS) {
            System.exit(exitCode);
        }
    }

    /**
     * Запускает приложение с обработкой ошибок.
     *
     * @param args аргументы командной строки
     * @return код завершения (0 = успех)
     */
    private static int runApplication(String[] args) {
        try {
            AppConfiguration config = AppConfiguration.fromArgs(args);
            config.validateInputFileExists();

            TextAnalyzer analyzer = new TextAnalyzer(config.getInputPath());
            List<WordStat> stats = analyzer.analyze();

            if (stats.isEmpty()) {
                log.info("Файл пуст или не содержит слов для анализа");
                System.out.println("Файл пуст или не содержит слов.");
                return EXIT_CODE_SUCCESS;
            }

            CsvReportWriter writer = new CsvReportWriter();
            writer.write(config.getOutputPath().toString(), stats);

            printResults(stats, config.getOutputPath());
            return EXIT_CODE_SUCCESS;

        } catch (InvalidInputException e) {
            log.error("Ошибка входных данных: {}", e.getMessage());
            System.err.println("Ошибка: " + e.getMessage());
            printUsage();
            return EXIT_CODE_INVALID_ARGS;

        } catch (FileOperationException e) {
            log.error("Ошибка операции с файлом: {}", e.getMessage());
            System.err.println("Ошибка файла: " + e.getMessage());
            return EXIT_CODE_FILE_ERROR;

        } catch (IOException e) {
            log.error("Ошибка ввода-вывода: {}", e.getMessage(), e);
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            return EXIT_CODE_PROCESSING_ERROR;

        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
            System.err.println("Внутренняя ошибка: " + e.getMessage());
            return EXIT_CODE_PROCESSING_ERROR;
        }
    }

    private static void printResults(List<WordStat> stats, java.nio.file.Path outputPath) {
        long wordCount = stats.stream().mapToInt(WordStat::count).sum();
        System.out.println("Обработано слов: " + wordCount);
        System.out.println("Уникальных слов: " + stats.size());
        System.out.println("CSV файл создан: " + outputPath);
    }

    private static void printUsage() {
        System.out.println("Использование: java WordFrequencyApp [путь_к_файлу]");
        System.out.println("  Если путь не указан, используется файл по умолчанию: test.txt");
    }
}
