package com.example.lab0;

import com.example.lab0.analyzer.TextAnalyzer;
import com.example.lab0.config.AppConfiguration;
import com.example.lab0.constants.AppConstants;
import com.example.lab0.exception.FileOperationException;
import com.example.lab0.exception.InvalidInputException;
import com.example.lab0.model.TextAnalysisResult;
import com.example.lab0.writer.CsvReportWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Точка входа в приложение для анализа текста и генерации CSV-отчета.
 * Реализует полную валидацию входных данных и обработку ошибок.
 * Использует внедрение зависимостей для гибкости и тестируемости.
 */
@Slf4j
public class WordFrequencyApp {

    private final AppConfiguration config;
    private final TextAnalyzer analyzer;
    private final CsvReportWriter reportWriter;

    /**
     * Создает приложение с конфигурацией по умолчанию.
     *
     * @param args аргументы командной строки
     */
    public WordFrequencyApp(String[] args) {
        this.config = AppConfiguration.fromArgs(args);
        this.analyzer = new TextAnalyzer(config.getInputPath());
        this.reportWriter = new CsvReportWriter();
    }

    /**
     * Создает приложение с внедренными зависимостями.
     * Позволяет использовать кастомные реализации для тестирования.
     *
     * @param config        конфигурация приложения
     * @param analyzer      анализатор текста
     * @param reportWriter  записчик отчетов
     */
    public WordFrequencyApp(
            AppConfiguration config,
            TextAnalyzer analyzer,
            CsvReportWriter reportWriter
    ) {
        this.config = config;
        this.analyzer = analyzer;
        this.reportWriter = reportWriter;
    }

    /**
     * Запускает приложение.
     *
     * @return код завершения
     */
    public int run() {
        try {
            config.validateInputFileExists();

            TextAnalysisResult result = analyzer.analyze();

            if (result.isEmpty()) {
                log.info("Файл пуст или не содержит слов для анализа");
                System.out.println("Файл пуст или не содержит слов.");
                return AppConstants.EXIT_CODE_SUCCESS;
            }

            reportWriter.write(config.getOutputPath(), result.getWordStats());

            printResults(result, config.getOutputPath());
            return AppConstants.EXIT_CODE_SUCCESS;

        } catch (InvalidInputException e) {
            log.error("Ошибка входных данных: {}", e.getMessage());
            printUsage();
            return AppConstants.EXIT_CODE_INVALID_ARGS;

        } catch (FileOperationException e) {
            log.error("Ошибка операции с файлом: {}", e.getMessage());
            return AppConstants.EXIT_CODE_FILE_ERROR;

        } catch (IOException e) {
            log.error("Ошибка ввода-вывода: {}", e.getMessage(), e);
            return AppConstants.EXIT_CODE_PROCESSING_ERROR;

        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage(), e);
            return AppConstants.EXIT_CODE_PROCESSING_ERROR;
        }
    }

    private void printResults(TextAnalysisResult result, Path outputPath) {
        System.out.println("Обработано слов: " + result.getTotalWords());
        System.out.println("Уникальных слов: " + result.getUniqueWords());
        System.out.println("CSV файл создан: " + outputPath);
    }

    private static void printUsage() {
        System.out.println("Использование: java WordFrequencyApp [путь_к_файлу]");
        System.out.println("  Если путь не указан, используется файл по умолчанию: " + AppConstants.DEFAULT_INPUT_FILE);
    }

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки:
     *             - 0 аргументов: используется файл по умолчанию (test.txt)
     *             - 1 аргумент: путь к входному файлу
     *             - >1 аргументов: ошибка
     */
    public static void main(String[] args) {
        WordFrequencyApp app = new WordFrequencyApp(args);
        int exitCode = app.run();
        if (exitCode != AppConstants.EXIT_CODE_SUCCESS) {
            System.exit(exitCode);
        }
    }
}
