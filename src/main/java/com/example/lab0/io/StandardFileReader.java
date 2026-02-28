package com.example.lab0.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Стандартная реализация FileReader для чтения файлов через NIO.
 */
public class StandardFileReader implements FileReader {

    @Override
    public String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
