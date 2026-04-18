package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exception.NoHintsException;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {
    private WordleGame game;
    private List<String> testWords;
    private String testAnswer;
    private String wrongWord;

    @BeforeEach
    void beforeEach() {
        testWords = List.of("кофта", "берег", "актер");
        PrintWriter consoleWriter = new PrintWriter(System.out);
        LogWriter logWriter = new LogWriter(consoleWriter);
        WordleDictionary wordleDictionary = new WordleDictionary(testWords, logWriter);
        game = new WordleGame(wordleDictionary, logWriter);
        testAnswer = game.getAnswer();
        wrongWord = "берег";
        if (wrongWord.equals(testAnswer)) {
            wrongWord = "актер";
        }
    }

    @Test
    void isWinReturnsTrue() {
        assertTrue(game.isWin(testAnswer));
    }

    @Test
    void isWinReturnsFalse() {
        assertFalse(game.isWin(wrongWord));
    }

    @Test
    void gameStepDecrementsSteps() {
        int stepsBefore = game.getSteps();
        game.gameStep(wrongWord);
        int stepsAfter = game.getSteps();

        assertEquals(stepsBefore - 1, stepsAfter);
    }

    @Test
    void getRandomHintReturnsValidWord() throws NoHintsException {
        String hint = game.getRandomHint();
        assertTrue(testWords.contains(hint));
    }

    @Test
    void getRandomHintThrowsException() {
        boolean exceptionThrown = false;
        try {
            for (int i = 0; i <= testWords.size(); i++) {
                game.getRandomHint();
            }
        } catch (NoHintsException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }
}
