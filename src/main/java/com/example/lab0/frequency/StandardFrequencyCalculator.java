package com.example.lab0.frequency;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Стандартная реализация калькулятора частоты через группировку элементов.
 */
public class StandardFrequencyCalculator implements FrequencyCalculator<String> {

    @Override
    public Map<String, Long> calculate(List<String> items) {
        if (items == null || items.isEmpty()) {
            return Map.of();
        }

        return items.stream()
                .collect(Collectors.groupingBy(
                        item -> item,
                        Collectors.counting()
                ));
    }
}
