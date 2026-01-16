package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
    }

    public boolean isGuessCorrect(String guess) {
        return guess.equals(answer);
    }

    class GameException extends Exception {
        public GameException(String message) {
            super(message);
        }
    }

    class InvalidGuessException extends GameException {
        public InvalidGuessException(String message) {
            super(message);
        }
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        this.answer = dictionary.getRandomWordOfLength(5);
        // Получаем случайное слово из словаря this.steps = 0; // количество шагов final
         int MAX_ATTEMPTS = 6; // Максимальное количество попыток
        System.out.println("Игра началась! Загадано слово."); String guess = "";

        while (steps < MAX_ATTEMPTS) {
            steps++;
            System.out.print("Попытка " + steps + ". Введите слово: ");
            guess = scanner.nextLine().trim();
            processGuess(guess); // Этот вызов должен быть здесь

            if (guess.length() != answer.length()) {
                System.out.println("Длина введённого слова не соответствует длине загаданного слова. Попробуйте ещё раз.");
                continue;
            }

            if (isGuessCorrect(guess)) {
                System.out.println("Поздравляем! Вы угадали слово за " + steps + " шагов.");
                break;
            } else {
                System.out.println("Неверно! Попробуйте еще раз.");
                String feedback = getFeedback(guess); // Получаем подсказку
                System.out.println("Подсказка: " + feedback); // Выводим подсказку
            }
        }

        if (steps == MAX_ATTEMPTS && !isGuessCorrect(guess)) {
            System.out.println("Вы не смогли угадать слово за " + MAX_ATTEMPTS + " попыток. Игра окончена.");
        }
    }

    private String getFeedback(String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);
            if (guessChar == answer.charAt(i)) {
                feedback.append("+");
            } else if (answer.contains(String.valueOf(guessChar))) {
                if (countOccurrences(guessChar, guess) <= countOccurrences(guessChar, answer)) {
                    feedback.append("^");
                } else {
                    feedback.append("-");
                }
            } else {
                feedback.append("-");
            }
        }
        return feedback.toString();
    }

    private int countOccurrences(char ch, String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }

    private int countCorrectLetters(String guess) {
        int count = 0;
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    private List<String> userGuesses = new ArrayList<>(); // Все попытки пользователя
    private List<String> feedbackHistory = new ArrayList<>(); // История подсказок
     // Словарь слов

    public void processGuess(String guess) {
        if (guess.isEmpty()) {
            // Если пользователь нажал Enter в пустой строке, показать подсказку
            String hint = getHint();
            System.out.println("Подсказка: " + hint);
        } else {
            userGuesses.add(guess);
            String feedback = getFeedback(guess); // Получаем подсказку для текущего слова
            feedbackHistory.add(feedback);
            System.out.println("Подсказка: " + feedback);

            System.out.println("Добавлено предположение: " + guess);
            System.out.println("Добавлен фидбэк: " + feedback);
        }
    }

    private String getHint() {
        List<String> potentialHints = new ArrayList<>();

        // Собираем информацию о правильных буквах и позициях
        for (int i = 0; i < userGuesses.size(); i++) {
            String guess = userGuesses.get(i);
            String feedback = feedbackHistory.get(i);

            for (int j = 0; j < guess.length(); j++) {
                if (feedback.charAt(j) == '+') {
                    // Буква на правильной позиции
                    potentialHints.add(guess.substring(j, j + 1));
                }
            }
        }

        // Фильтруем слова из словаря, которые соответствуют критериям
        List<String> filteredWords = dictionary.getWords().stream()
                .filter(word -> {
                    for (String hint : potentialHints) {
                        if (!word.contains(hint)) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList();

        // Выбираем случайное слово для подсказки
        if (!filteredWords.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(filteredWords.size());
            return filteredWords.get(randomIndex);
        } else {
            return "Подсказку найти не удалось.";
        }
    }
}
