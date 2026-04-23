package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.InvalidWordException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {
    private static List<String> testWords;
    private WordleDictionary wordleDictionary;

    @BeforeAll
    static void beforeAll() {
        testWords = List.of("кофта", "козел", "парус", "берег", "актер", "почта", "мачта", "паром");
    }

    @BeforeEach
    void beforeEach() {
        PrintWriter consoleWriter = new PrintWriter(System.out);
        LogWriter logWriter = new LogWriter(consoleWriter);
        wordleDictionary = new WordleDictionary(testWords, logWriter);
    }

    @Test
    void getHintStringReturnCorrectStringHint() {
        String hint1 = wordleDictionary.getHintString("берет", "берег");
        assertEquals("++++-", hint1);

        String hint2 = wordleDictionary.getHintString("козел", "мачта");
        assertEquals("-----", hint2);

        String hint3 = wordleDictionary.getHintString("паром", "почта");
        assertEquals("+^-^-", hint3);
    }

    @Test
    void checkUserWordDoesNotThrowException() {
        boolean exceptionThrown = false;
        try {
            wordleDictionary.checkUserWord("кофта");
            wordleDictionary.checkUserWord("актер");
            wordleDictionary.checkUserWord("берег");
        } catch (Exception e) {
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    void testCheckUserWordThrowsInvalidWordExceptionWithEmptyWord() {
        boolean emptyWord = false;

        try {
            wordleDictionary.checkUserWord("     ");
        } catch (InvalidWordException e) {
            emptyWord = true;
        } catch (Exception ignored) {
        }

        assertTrue(emptyWord);
    }

    @Test
    void testCheckUserWordThrowsInvalidWordExceptionWithWrongLength() {
        boolean wrongLength = false;

        try {
            wordleDictionary.checkUserWord("ком");
        } catch (InvalidWordException e) {
            wrongLength = true;
        } catch (Exception ignored) {
        }

        assertTrue(wrongLength);
    }

    @Test
    void testCheckUserWordThrowsWordNotFoundInDictionaryException() {
        boolean notFound = false;
        try {
            wordleDictionary.checkUserWord("gamer");
        } catch (WordNotFoundInDictionaryException e) {
            notFound = true;
        } catch (Exception ignored) {
        }

        assertTrue(notFound);
    }

    @Test
    void formatWordNormalizesCorrectly() {
        assertEquals("актер", wordleDictionary.formatWord("АКТЕР"));
        assertEquals("актер", wordleDictionary.formatWord("АкТеР"));
        assertEquals("актер", wordleDictionary.formatWord("актёр"));
    }

    @Test
    void getPossibleWordsReturnsAllWords() {
        Map<String, String> emptyHistory = new LinkedHashMap<>();
        List<String> result = wordleDictionary.getPossibleWords(emptyHistory);

        int expected = testWords.size();

        assertEquals(expected, result.size());
        assertTrue(result.containsAll(testWords));
    }

    @Test
    void getPossibleWordsFiltersByHints() {
        Map<String, String> history = new LinkedHashMap<>();
        history.put("козел", "-----");

        List<String> result = wordleDictionary.getPossibleWords(history);

        assertEquals(2, result.size());

        assertTrue(result.contains("мачта"));
        assertTrue(result.contains("парус"));
        assertFalse(result.contains("кофта"));
        assertFalse(result.contains("берег"));
    }
}
