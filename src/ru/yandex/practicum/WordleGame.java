package ru.yandex.practicum;

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
        this.answer = dictionary.getRandomWordOfLength(5); // Получаем случайное слово из словаря
        this.steps = 0; // количество шагов
        final int MAX_ATTEMPTS = 6; // Максимальное количество попыток

        System.out.println("Игра началась! Загадано слово.");
        String guess = "";

        while (steps < MAX_ATTEMPTS) {
            steps++;
            System.out.print("Попытка " + steps + ". Введите слово: ");
            guess = scanner.nextLine().trim();

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
}
