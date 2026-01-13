import org.junit.jupiter.api.Test;
import ru.yandex.practicum.WordleDictionary;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleTest {
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
}
