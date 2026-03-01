package com.example.lab0.validation;

import com.example.lab0.exception.FileOperationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Валидатор файлов.
 * Инкапсулирует всю логику проверки существования и доступности файлов.
 */
public final class FileValidator {

    private FileValidator() {
        throw new UnsupportedOperationException("Класс валидатора не может быть инстанцирован");
    }

    /**
     * Проверяет, что файл существует, доступен для чтения и не является директорией.
     *
     * @param path путь к файлу
     * @throws FileOperationException если файл не проходит проверку
     */
    public static void validateReadableFile(Path path) {
        validateExists(path);
        validateReadable(path);
        validateNotDirectory(path);
    }

    /**
     * Проверяет существование файла.
     *
     * @param path путь к файлу
     * @throws FileOperationException если файл не существует
     */
    public static void validateExists(Path path) {
        if (!Files.exists(path)) {
            throw new FileOperationException("Файл не найден: " + path);
        }
    }

    /**
     * Проверяет доступность файла для чтения.
     *
     * @param path путь к файлу
     * @throws FileOperationException если файл не доступен для чтения
     */
    public static void validateReadable(Path path) {
        if (!Files.isReadable(path)) {
            throw new FileOperationException("Файл недоступен для чтения: " + path);
        }
    }

    /**
     * Проверяет, что путь указывает на файл, а не на директорию.
     *
     * @param path путь
     * @throws FileOperationException если путь указывает на директорию
     */
    public static void validateNotDirectory(Path path) {
        if (Files.isDirectory(path)) {
            throw new FileOperationException("Путь указывает на директорию, а не файл: " + path);
        }
    }

    /**
     * Проверяет размер файла.
     *
     * @param path      путь к файлу
     * @param maxSizeBytes максимальный размер в байтах
     * @throws FileOperationException если файл слишком большой
     */
    public static void validateFileSize(Path path, long maxSizeBytes) {
        try {
            long fileSize = Files.size(path);
            if (fileSize > maxSizeBytes) {
                throw new FileOperationException(
                    String.format("Файл слишком большой (%.2f MB). Максимум: %d MB",
                        fileSize / (1024.0 * 1024.0), maxSizeBytes / (1024 * 1024))
                );
            }
        } catch (IOException e) {
            throw new FileOperationException("Не удалось проверить размер файла: " + path, e);
        }
    }
}
