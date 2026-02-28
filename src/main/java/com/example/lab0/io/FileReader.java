package com.example.lab0.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Интерфейс для чтения содержимого файлов.
 * Позволяет абстрагироваться от конкретного способа чтения и упрощает тестирование.
 */
@FunctionalInterface
public interface FileReader {

    /**
     * Читает содержимое файла по указанному пути.
     *
     * @param path путь к файлу
     * @return содержимое файла как строка
     * @throws IOException если файл не может быть прочитан
     */
    String read(Path path) throws IOException;
}
