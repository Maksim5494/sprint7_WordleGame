package ru.yandex.practicum;

import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WordleGame {
    private String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private final Wordle view;

    private final Set<Character> absentLetters = new HashSet<>();
    private final Map<Integer, Character> confirmedPositions = new HashMap<>();
    private final Map<Character, Set<Integer>> wrongPositions = new HashMap<>();
    private final List<String> userGuesses = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.log = log;
        this.view = new Wordle();
    }

    public WordleGame(WordleDictionary dictionary) throws IOException {
        this(dictionary, new PrintWriter(new FileWriter("game_log.txt", true)));
    }

    public void startGame() {
        this.answer = dictionary.getRandomWordOfLength(5).toLowerCase();
        int maxAttempts = 6;
        Scanner scanner = new Scanner(System.in);

        view.displayMessage("Игра началась! Загадано слово из 5 букв.");

        while (steps < maxAttempts) {
            view.displayMessage("\nПопытка " + (steps + 1) + ". Введите слово (или Enter для подсказки):");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                view.displayMessage("Подсказка (лучшее слово): " + getHint());
                continue;
            }

            if (input.length() != 5) {
                view.displayMessage("Ошибка: длина слова должна быть 5 символов.");
                continue;
            }

            if (!dictionary.containsWord(input)) {
                view.displayMessage("Такого слова нет в словаре!");
                continue;
            }

            steps++;
            updateGameState(input);
            String feedback = getFeedback(input);
            userGuesses.add(input);

            log.println("Попытка " + steps + ": " + input + " | Результат: " + feedback);
            view.displayMessage("Результат: " + feedback);

            if (input.equals(answer)) {
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

    public void updateGameState(String guess) {
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            if (answer.charAt(i) == c) {
                confirmedPositions.put(i, c);
            } else if (answer.indexOf(c) != -1) {
                wrongPositions.computeIfAbsent(c, k -> new HashSet<>()).add(i);
            } else {
                absentLetters.add(c);
            }
        }
    }

    public String getFeedback(String guess) {
        char[] result = new char[5];
        Map<Character, Integer> targetCounts = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            char targetChar = answer.charAt(i);
            if (guess.charAt(i) == targetChar) {
                result[i] = '+';
            } else {
                result[i] = '-';
                targetCounts.put(targetChar, targetCounts.getOrDefault(targetChar, 0) + 1);
            }
        }

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

    public String getHint() {
        List<String> candidates = new ArrayList<>();

        for (String word : dictionary.getWords()) {
            if (word.length() != 5 || userGuesses.contains(word)) continue;

            boolean possible = true;

            for (char absent : absentLetters) {
                if (word.indexOf(absent) != -1) {
                    possible = false;
                    break;
                }
            }
            if (!possible) continue;

            for (Map.Entry<Integer, Character> entry : confirmedPositions.entrySet()) {
                if (word.charAt(entry.getKey()) != entry.getValue()) {
                    possible = false;
                    break;
                }
            }
            if (!possible) continue;

            for (Map.Entry<Character, Set<Integer>> entry : wrongPositions.entrySet()) {
                char c = entry.getKey();
                if (word.indexOf(c) == -1) { // Должна быть в слове
                    possible = false;
                    break;
                }
                for (int pos : entry.getValue()) { // Но не на этой позиции
                    if (word.charAt(pos) == c) {
                        possible = false;
                        break;
                    }
                }
            }

            if (possible) candidates.add(word);
        }

        if (candidates.isEmpty()) return "Слова не найдены";

        candidates.sort((a, b) -> {
            long countA = a.chars().distinct().count();
            long countB = b.chars().distinct().count();
            return Long.compare(countB, countA);
        });
        return candidates.get(0);
    }

    public void closeLog() {
        if (log != null) {
            log.flush();
            log.close();
        }
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}