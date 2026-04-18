package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryLoadException;
import ru.yandex.practicum.exception.GameRuntimeException;
import ru.yandex.practicum.exception.NoHintsException;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;
    private final WordleDictionary dictionary;
    private final Map<String, String> stepHistory;
    private final List<String> hintHistory;
    private final LogWriter logWriter;
    private int steps;

    public WordleGame(WordleDictionary dictionary, LogWriter logWriter) {
        this.logWriter = logWriter;
        this.dictionary = dictionary;
        answer = getWordforAnswer(dictionary.getWords());
        steps = 6;
        stepHistory = new LinkedHashMap<>();
        hintHistory = new ArrayList<>(6);
    }

    public String makeStep(String userWord) throws GameRuntimeException {
        if (userWord == null) {
            throw new GameRuntimeException("userWord = null");
        }
        if (steps <= 0) {
            throw new GameRuntimeException("Ходы закончились. Игра не может продолжаться");
        }
        if (userWord.length() != answer.length()) {
            throw new GameRuntimeException("Длины слов правильного ответа и ввода пользователя не совпадают");
        }
        steps--;
        String hint = dictionary.getHintString(userWord, answer);
        logWriter.write("Игровой ход. Введенное слово " + userWord + ". Строка подсказка " + hint);
        stepHistory.put(userWord, hint);
        return hint;
    }

    private String getWordforAnswer(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new DictionaryLoadException("Список доступных для игры слов пустой. Невозможно загадать слово");
        }
        Random random = new Random();
        int index = random.nextInt(words.size());
        String answer = words.get(index);
        logWriter.write("Загадано слово: " + answer);
        return answer;
    }

    public boolean isWin(String userWord) {
        return userWord.equals(answer);
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    public String getRandomHint() throws NoHintsException {
        logWriter.write("Игрок запросил подсказку подходящего слова");
        List<String> hints = dictionary.getPossibleWords(stepHistory);
        for (String hint : hints) {
            if (!hintHistory.contains(hint)) {
                hintHistory.add(hint);
                logWriter.write("Выбрана подсказка " + hint);
                return hint;
            }
        }
        throw new NoHintsException("Доступных подсказок не осталось");
    }
}
