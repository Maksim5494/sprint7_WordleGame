package ru.yandex.practicum;

import java.util.*;
import java.io.*;

public class WordleGame {
    private String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final Wordle view;
    private final List<String> userGuesses = new ArrayList<>();
    private final List<String> feedbackHistory = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary) throws IOException {
        this.dictionary = dictionary;
        this.log = new PrintWriter(new FileWriter("game_log.txt", true));
        this.view = new Wordle();
    }

    public void startGame() {
        this.answer = dictionary.getRandomWordOfLength(5).toLowerCase();
        int MAX_ATTEMPTS = 6;
        Scanner scanner = new Scanner(System.in);

        view.displayMessage("Игра началась! Загадано слово из 5 букв.");

        while (steps < MAX_ATTEMPTS) {
            view.displayMessage("\nПопытка " + (steps + 1) + ". Введите слово (или Enter для подсказки):");
            String guess = scanner.nextLine().trim().toLowerCase();

            if (guess.isEmpty()) {
                view.displayMessage("Подсказка (лучшее слово): " + getHint());
                continue;
            }

            if (guess.length() != 5) {
                view.displayMessage("Ошибка: длина слова должна быть 5 символов.");
                continue;
            }

            if (!dictionary.containsWord(guess)) {
                view.displayMessage("Такого слова нет в словаре!");
                continue;
            }

            steps++;
            String feedback = getFeedback(guess);
            userGuesses.add(guess);
            feedbackHistory.add(feedback);

            log.println("Попытка " + steps + ": " + guess + " | Результат: " + feedback);
            view.displayMessage("Результат: " + feedback);

            if (guess.equals(answer)) {
                view.displayMessage("Поздравляем! Вы угадали слово за " + steps + " шагов.");
                log.println("Игра выиграна.");
                closeLog();
                return;
            }
        }

        view.displayMessage("Вы не смогли угадать слово. Загаданное слово было: " + answer);
        log.println("Игра проиграна. Слово: " + answer);
        closeLog();
    }

    private String getFeedback(String guess) {
        char[] result = new char[5];
        java.util.Map<Character, Integer> targetCounts = new java.util.HashMap<>();

        // Сначала заполняем массив результатами '+' и считаем символы, которые не совпали точно
        for (int i = 0; i < 5; i++) {
            char targetChar = answer.charAt(i);
            if (guess.charAt(i) == targetChar) {
                result[i] = '+';
            } else {
                result[i] = '-';
                targetCounts.put(targetChar, targetCounts.getOrDefault(targetChar, 0) + 1);
            }
        }

        // Второй проход для определения символов на других позициях '^'
        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') continue;

            char guessChar = guess.charAt(i);
            if (targetCounts.getOrDefault(guessChar, 0) > 0) {
                result[i] = '^';
                targetCounts.put(guessChar, targetCounts.get(guessChar) - 1);
            }
        }

        return new String(result);
    }

    private String getHint() {
        // Упрощенная логика подсказки: ищем в словаре подходящее слово
        for (String word : dictionary.getWords()) {
            if (word.length() == 5 && !userGuesses.contains(word)) {
                return word;
            }
        }
        return "Слова не найдены";
    }

    public void closeLog() {
        log.flush();
        log.close();
    }
}
