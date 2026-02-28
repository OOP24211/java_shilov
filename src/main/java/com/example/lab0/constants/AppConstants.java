package com.example.lab0.constants;

/**
 * Константы приложения для анализа частоты слов.
 * Централизованное хранилище всех магических чисел и строковых констант.
 */
public final class AppConstants {

    // === Константы файлов ===
    public static final String DEFAULT_INPUT_FILE = "test.txt";
    public static final String CSV_OUTPUT_EXTENSION = ".csv";
    public static final String CSV_HEADER = "Слово,Частота,Частота (%)";

    // === Ограничения размеров ===
    public static final int MAX_FILE_SIZE_BYTES = 100 * 1024 * 1024; // 100 MB
    public static final int MAX_FILE_SIZE_MB = MAX_FILE_SIZE_BYTES / (1024 * 1024);
    public static final int MAX_PATH_LENGTH = 1000;
    public static final int MAX_FILENAME_LENGTH = 500;
    public static final int MAX_WORD_LENGTH = 500;

    // === Коды завершения ===
    public static final int EXIT_CODE_SUCCESS = 0;
    public static final int EXIT_CODE_INVALID_ARGS = 1;
    public static final int EXIT_CODE_FILE_ERROR = 2;
    public static final int EXIT_CODE_PROCESSING_ERROR = 3;

    // === Форматы и локали ===
    public static final String CSV_PERCENTAGE_FORMAT = "%.4f";
    public static final String CSV_ROW_FORMAT = "%s,%d,%.4f";

    // === Символы ===
    public static final char NULL_CHARACTER = '\0';
    public static final char DOUBLE_QUOTE = '"';
    public static final char COMMA = ',';
    public static final char NEWLINE = '\n';
    public static final char CARRIAGE_RETURN = '\r';

    // === Приватный конструктор для предотвращения инстанциации ===
    private AppConstants() {
        throw new UnsupportedOperationException("Класс констант не может быть инстанцирован");
    }
}
