package ru.yandex.practicum;

import java.util.*;
import java.util.logging.ConsoleHandler;

public class WordleGame {
    private String answer;
    private int steps;
    private WordleDictionary dictionary;
    private OutputHandler outputHandler; // Объявляем переменную на уровне класса

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.outputHandler = new ConsoleOutputHandler(); // Инициализируем переменную
    }

    class ConsoleOutputHandler implements OutputHandler {
        @Override
        public void displayMessage(String message) {
            System.out.println(message);
        }
        public class ConsoleReader {
            public String readInput() {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        }

    }
    public interface OutputHandler {
        void displayMessage(String message);
    }
    private Map<Integer, Set<Character>> errorPositions = new HashMap<>();

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
        ConsoleOutputHandler outputHandler = new ConsoleOutputHandler();
        ConsoleOutputHandler.ConsoleReader consoleReader = outputHandler.new ConsoleReader();

        this.answer = dictionary.getRandomWordOfLength(5);
        // Получаем случайное слово из словаря this.steps = 0; // количество шагов final
         int MAX_ATTEMPTS = 6; // Максимальное количество попыток
        Scanner scanner = new Scanner(System.in);
        outputHandler.displayMessage("Игра началась! Загадано слово.");
        String guess = "";
        //String input = consoleReader.readInput();

        while (steps < MAX_ATTEMPTS) {
            steps++;

            outputHandler.displayMessage("Попытка " + steps + ". Введите слово: ");
            guess = scanner.nextLine().trim();
            List<String> potentialHints = new ArrayList<>();

            if (guess.isEmpty()) {
                String hint = getHint();
                outputHandler.displayMessage("Подсказка: " + hint);
                continue; // Переходим к следующей итерации цикла
            }

            try {
                processGuess(guess);
            } catch (WordNotFoundInDictionary e) {
                outputHandler.displayMessage(e.getMessage()); }

            for (int i = 0; i < userGuesses.size(); i++) {
                String currentGuess = userGuesses.get(i);
                String feedback = feedbackHistory.get(i);

                for (int j = 0; j < currentGuess.length(); j++) {
                    if (feedback.charAt(j) == '+') {
                        potentialHints.add(currentGuess.substring(j, j + 1));
                    }
                }
            }

            Set<Character> invalidChars = new HashSet<>();
            for (String currentGuess : userGuesses) {
                for (char c : currentGuess.toCharArray()) {
                    // Проверяем, что буква отсутствует в правильной позиции во всех feedback
                    if (!feedbackHistory.stream().anyMatch(feedback -> feedback.indexOf(c) != -1)) {
                        invalidChars.add(c);
                    }
                }
            }

            if (guess.length() != answer.length()) {
                outputHandler.displayMessage("Длина введённого слова не соответствует длине загаданного слова. Попробуйте ещё раз.");
                continue;
            }

            if (isGuessCorrect(guess)) {
                outputHandler.displayMessage("Поздравляем! Вы угадали слово за " + steps + " шагов.");
                break;
            }
        }

        if (steps == MAX_ATTEMPTS && !isGuessCorrect(guess)) {
            outputHandler.displayMessage("Вы не смогли угадать слово за " + MAX_ATTEMPTS + " попыток. Игра окончена.");
        }
    }

    public class ConsoleHandler {
        public void displayMessage(String message) {
            System.out.println(message);
        }

        public String readInput() {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
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

    class InvalidWordLengthException extends Exception {
        public InvalidWordLengthException(String message) {
            super(message);
        }
    }

    private List<String> userGuesses = new ArrayList<>(); // Все попытки пользователя
    private List<String> feedbackHistory = new ArrayList<>(); // История подсказок
     // Словарь слов

    public void processGuess(String guess) throws WordNotFoundInDictionary {

        if (!dictionary.containsWord(guess)) {
            throw new WordNotFoundInDictionary("Слово не найдено в словаре.");
        }

        if (guess.isEmpty()) {
            // Если пользователь нажал Enter в пустой строке, показать подсказку
            String hint = getHint();
            outputHandler.displayMessage("Подсказка: " + hint);
        } else {
            userGuesses.add(guess);
            String feedback = getFeedback(guess); // Получаем подсказку для текущего слова
            feedbackHistory.add(feedback);
            outputHandler.displayMessage("Подсказка: " + feedback);
        }

        if (!isGuessCorrect(guess)) {
            for (int i = 0; i < guess.length(); i++) {
                char guessChar = guess.charAt(i);
                if (guessChar != answer.charAt(i)) {
                    errorPositions.computeIfAbsent(i, k -> new HashSet<>()).add(guessChar);
                }
            }
        }
    }

    private String getHint() {
            List<String> potentialHints = new ArrayList<>();
            Map<Character, Integer> letterFrequency = new HashMap<>();

            // Собираем информацию о правильных буквах и позициях
            for (int i = 0; i < userGuesses.size(); i++) {
                String guess = userGuesses.get(i);
                String feedback = feedbackHistory.get(i);

                for (int j = 0; j < guess.length(); j++) {
                    if (feedback.charAt(j) == '+') {
                        potentialHints.add(guess.substring(j, j + 1));
                    }
                }
            }

            // Подсчитываем частоту встречаемости букв в оставшихся словах
            for (String word : dictionary.getWords()) {
                for (char c : word.toCharArray()) {
                    letterFrequency.put(c, letterFrequency.getOrDefault(c, 0) + 1);
                }
            }

            // Фильтруем слова из словаря, которые соответствуют критериям
            List<String> filteredWords = dictionary.getWords().stream()
                    .filter(word -> word.length() == 5)
                    .filter(word -> {
                        for (String hint : potentialHints) {
                            if (!word.contains(hint)) {
                                return false;
                            }
                        }
                        for (int i = 0; i < word.length(); i++) {
                            char c = word.charAt(i);
                            if (errorPositions.containsKey(i) && errorPositions.get(i).contains(c)) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .sorted((word1, word2) -> {
                        int score1 = 0;
                        int score2 = 0;
                        for (char c : word1.toCharArray()) {
                            score1 += letterFrequency.get(c);
                        }
                        for (char c : word2.toCharArray()) {
                            score2 += letterFrequency.get(c);
                        }
                        return Integer.compare(score2, score1); // Сортировка по убыванию частоты
                    })
                    .toList();

            // Выбираем первое слово из отсортированного списка
        if (!filteredWords.isEmpty()) {
            return filteredWords.get(0);
        } else {
            return "Подсказку найти не удалось.";
            }
        }

    public class WordNotFoundInDictionary extends Exception {
        public WordNotFoundInDictionary(String message) {
            super(message);
        }
    }

}
