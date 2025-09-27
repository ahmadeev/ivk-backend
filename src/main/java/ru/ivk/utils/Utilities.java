package ru.ivk.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
    public static <T> T findMostFrequent(List<T> list) {
        if (list == null || list.isEmpty()) return null;
        Map<T, Integer> frequencies = new HashMap<>();
        for (T element : list) {
            frequencies.put(element, frequencies.getOrDefault(element, 0) + 1);
        }

        T mostFrequent = null;
        int maxFrequency = 0;

        for (Map.Entry<T, Integer> entry : frequencies.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }

        return mostFrequent;
    }
}
