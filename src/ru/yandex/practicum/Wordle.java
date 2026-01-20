package ru.yandex.practicum;

import java.io.IOException;
import java.util.List;

public class Wordle {
    public static void main(String[] args) {
        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            // Убедитесь, что файл words.txt лежит в корне проекта или укажите полный путь
            List<String> loadedWords = loader.loadDictionary("words.txt");
            WordleDictionary dictionary = new WordleDictionary(loadedWords);
            WordleGame game = new WordleGame(dictionary);
            game.startGame();
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке словаря: " + e.getMessage());
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}


