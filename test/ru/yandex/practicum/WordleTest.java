import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionary;
import ru.yandex.practicum.WordleGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
    private static WordleDictionary dictionary; // Объявляем переменную на уровне класса
    private WordleGame game;

    @Test
    void testGetRandomWordOfLength() {
        // Создаём список слов для теста
        List<String> words = new ArrayList<>();
        words.add("test");
        words.add("example");
        words.add("word");
        words.add("java");

        WordleDictionary dictionary = new WordleDictionary(words);
        String randomWord = dictionary.getRandomWordOfLength(4);
        assertEquals(4, randomWord.length());
        assertTrue(words.contains(randomWord));
    }

    @BeforeEach
    void init() throws IOException {
        game = new WordleGame(dictionary);
        game.startGame();
    }
    @Test
    void testCorrectGuess() {
        String guess = "слово";
        game.processGuess(guess);
        assertTrue(game.isGuessCorrect(guess));
        // Дополнительные проверки состояния игры
    }

}
