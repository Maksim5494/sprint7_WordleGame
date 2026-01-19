package ru.yandex.practicum;

import java.io.IOException;
import java.util.List;

public class Wordle {
    public static void main(String[] args) {

        try {
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            List<String> loadedWords = loader.loadDictionary("C:/Users/Макс/IdeaProjects/sprint7_WordleGame/words.txt");
            WordleDictionary dictionary = new WordleDictionary(loadedWords);
            WordleGame game = new WordleGame(dictionary);
            game.startGame();

        } catch (IOException e) {
            System.out.println("Ошибка при загрузке словаря: " + e.getMessage());
        }
    }
}




