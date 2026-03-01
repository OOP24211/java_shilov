package com.example.lab0.config;

import com.example.lab0.constants.AppConstants;
import com.example.lab0.exception.InvalidInputException;
import com.example.lab0.validation.PathValidator;

import java.nio.file.Path;

/**
 * Конфигурация приложения с валидацией параметров.
 * Использует валидаторы для устранения дублирования кода.
 */
public final class AppConfiguration {

    private final Path inputPath;
    private final Path outputPath;

    private AppConfiguration(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    /**
     * Создает и валидирует конфигурацию из аргументов командной строки.
     *
     * @param args аргументы командной строки
     * @return валидная конфигурация
     * @throws InvalidInputException если аргументы невалидны
     */
    public static AppConfiguration fromArgs(String[] args) {
        if (args == null) {
            throw new InvalidInputException("Аргументы командной строки не могут быть null");
        }

        String inputFilePath = parseInputFile(args);
        Path inputPath = PathValidator.validate(inputFilePath).toAbsolutePath().normalize();
        Path outputPath = buildOutputPath(inputPath);

        return new AppConfiguration(inputPath, outputPath);
    }

    private static String parseInputFile(String[] args) {
        if (args.length == 0) {
            return AppConstants.DEFAULT_INPUT_FILE;
        }

        if (args.length > 1) {
            throw new InvalidInputException(
                "Ожидается не более одного аргумента (путь к файлу). Получено: " + args.length
            );
        }

        String inputFilePath = args[0].trim();
        if (inputFilePath.isEmpty()) {
            throw new InvalidInputException("Путь к файлу не может быть пустым");
        }

        return inputFilePath;
    }

    private static Path buildOutputPath(Path inputPath) {
        String fileName = inputPath.getFileName().toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        String nameWithoutExt = (lastDotIndex > 0) ? fileName.substring(0, lastDotIndex) : fileName;
        return inputPath.resolveSibling(nameWithoutExt + AppConstants.CSV_OUTPUT_EXTENSION);
    }

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    @Override
    public String toString() {
        return "AppConfiguration{inputPath=" + inputPath + ", outputPath=" + outputPath + '}';
    }
}
