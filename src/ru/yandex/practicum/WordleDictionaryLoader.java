package ru.yandex.practicum;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    public List<String> loadDictionary(String fileName) throws IOException {
        List<String> dictionary = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line);
            }
        }
        return dictionary;
    }
}

