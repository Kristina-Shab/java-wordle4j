package ru.yandex.practicum;

import java.util.List;
import java.util.Random;

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
    private int steps;

    public WordleGame(WordleDictionary dictionary) {
        answer = getWordforAnswer(dictionary.getWords());
        steps = 6;
        this.dictionary = dictionary;
    }

    public String gameStep(String userWord) {
        steps--;
        return dictionary.getHintString(userWord, answer);
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
}
