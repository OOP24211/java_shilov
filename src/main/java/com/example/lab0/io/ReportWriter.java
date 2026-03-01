package com.example.lab0.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Интерфейс для записи отчётов.
 * Позволяет использовать различные форматы вывода (CSV, JSON, XML и т.д.).
 *
 * @param <T> тип данных для записи
 */
@FunctionalInterface
public interface ReportWriter<T> {

    /**
     * Записывает данные в файл.
     *
     * @param path  путь к выходному файлу
     * @param data  данные для записи
     * @throws IOException если файл не может быть записан
     */
    void write(Path path, List<T> data) throws IOException;
}
