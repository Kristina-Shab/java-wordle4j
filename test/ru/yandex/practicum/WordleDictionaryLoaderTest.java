package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.DictionaryLoadException;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryLoaderTest {
    private WordleDictionaryLoader wordleDictionaryLoader;
    private WordleDictionary wordleDictionary;

    @BeforeEach
    public void beforeEach() {
        PrintWriter consoleWriter = new PrintWriter(System.out);
        LogWriter logWriter = new LogWriter(consoleWriter);
        wordleDictionaryLoader = new WordleDictionaryLoader(logWriter);
        wordleDictionary = wordleDictionaryLoader.getDictionary("test_words.txt");
    }

    @Test
    void getDictionaryLoadsWordsCorrectLength() {
        List<String> words = wordleDictionary.getWords();

        for (String word : words) {
            assertEquals(5, word.length());
        }
    }

    @Test
    void getDictionaryLoadsWordsCorrectFormat() {
        List<String> words = wordleDictionary.getWords();

        for (String word : words) {
            assertEquals(word.toLowerCase(), word);
            assertFalse(word.contains("ё"));
        }
    }

    @Test
    void getDictionaryLoadsOnlyValidWords() {
        List<String> words = wordleDictionary.getWords();
        int expected = 3;
        assertEquals(expected, words.size());
        assertTrue(words.contains("почта"));
        assertTrue(words.contains("мачта"));
        assertTrue(words.contains("актер"));
    }

    @Test
    void getDictionaryThrowsExceptionIfFileNotFound() {
        boolean exceptionThrown = false;
        try {
            wordleDictionaryLoader.getDictionary("non_existent_file.txt");
        } catch (DictionaryLoadException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
