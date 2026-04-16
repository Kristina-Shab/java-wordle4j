package ru.yandex.practicum;

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
    private final List<String> hintHystory;
    private int steps;

    public WordleGame(WordleDictionary dictionary) {
        answer = getWordforAnswer(dictionary.getWords());
        steps = 6;
        this.dictionary = dictionary;
        stepHistory = new LinkedHashMap<>();
        hintHystory = new ArrayList<>(6);
    }

    public String gameStep(String userWord) {
        steps--;
        String hint = dictionary.getHintString(userWord, answer);
        stepHistory.put(userWord, hint);
        return hint;
    }

    private String getWordforAnswer(List<String> words) {    // + проверка, что список не пустой
        Random random = new Random();
        int index = random.nextInt(words.size());
        return words.get(index);
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

    public String getRandomHint() {
        List<String> hints = dictionary.getPossibleWords(stepHistory);
        for (int i = 0; i < hints.size(); i++) {
            if (!hintHystory.contains(hints.get(i))) {
                hintHystory.add(hints.get(i));
                return hints.get(i);
            }
        }
        throw new RuntimeException();
    }
}
