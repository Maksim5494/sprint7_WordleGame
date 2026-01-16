package ru.yandex.practicum;

import java.util.List;
import java.util.ArrayList;

public class WordleDictionary {
    private List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>();
        for (String word : words) {
            this.words.add(WordProcessor.processWord(word));
        }
    }

    public String getRandomWordOfLength(int length) {
        List<String> validWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() == length) {
                validWords.add(word);
            }
        }

        if (validWords.isEmpty()) {
            throw new RuntimeException("Нет слов заданной длины в словаре.");
        }

        int randomIndex = (int) (Math.random() * validWords.size());
        return validWords.get(randomIndex);
    }

    static class WordProcessor {
        public static String processWord(String word) {
            // Приводим слово к нижнему регистру
            word = word.toLowerCase();

            // Заменяем ё на е
            word = word.replace('ё', 'е');

            return word;
        }
    }

    public List<String> getWords() {
        return words;
    }

}


