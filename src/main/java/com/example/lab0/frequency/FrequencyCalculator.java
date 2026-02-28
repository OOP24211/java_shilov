package com.example.lab0.frequency;

import java.util.List;
import java.util.Map;

/**
 * Интерфейс для вычисления частоты элементов.
 * Позволяет использовать различные алгоритмы подсчёта.
 *
 * @param <T> тип элементов
 */
@FunctionalInterface
public interface FrequencyCalculator<T> {

    /**
     * Вычисляет частоту каждого элемента в списке.
     *
     * @param items список элементов
     * @return карта с частотой каждого элемента
     */
    Map<T, Long> calculate(List<T> items);
}
