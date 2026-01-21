package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.util.List;

class WordleTest {
    private WordleGame game;
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        // Используем небольшой словарь для тестов
        dictionary = new WordleDictionary(List.of("арбуз", "плита", "палка", "майор", "мишка"));
        // Направляем лог в консоль
        PrintWriter out = new PrintWriter(System.out);
        game = new WordleGame(dictionary, out);
    }

    @Test
    void testFeedbackCorrectWord() {
        game.setAnswer("арбуз");
        assertEquals("+++++", game.getFeedback("арбуз"), "Все буквы должны быть отмечены '+'");
    }

    @Test
    void testFeedbackPartialMatch() {
        game.setAnswer("плита");
        // П - совпало (+), Л - нет, И - нет, Т - есть, но в другом месте (^), А - есть в другом месте (^)
        // Но в слове "палка" для "плита": П(0)+, А(1)^, Л(2)^ ...
        String feedback = game.getFeedback("палка");
        assertTrue(feedback.contains("+") && feedback.contains("^"));
    }

    @Test
    void testHintFiltering() {
        game.setAnswer("плита");
        // Имитируем ход, где 'м', 'а', 'й', 'о', 'р' отсутствуют
        game.updateGameState("майор");

        String hint = game.getHint();
        // Из словаря ["арбуз", "плита", "палка", "майор", "мишка"] 
        // "майор" исключен (был введен), "арбуз" содержит 'р' (исключен), 
        // "палка" содержит 'а' (исключен в Wordle обычно, если серая),
        // "мишка" содержит 'м'.

        assertNotNull(hint);
        assertNotEquals("майор", hint);
    }

    @Test
    void testDictionaryValidation() {
        assertTrue(dictionary.containsWord("плита"));
        assertFalse(dictionary.containsWord("слово"));
    }

    @Test
    void testDeadlockMessage() {
        game.updateGameState("плита");
        game.updateGameState("арбуз");
        // После исключения почти всех букв в нашем маленьком словаре
        String hint = game.getHint();
        assertTrue(hint.equals("Слова не найдены") || dictionary.containsWord(hint));
    }
}