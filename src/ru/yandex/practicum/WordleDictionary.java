package ru.yandex.practicum;

import ru.yandex.practicum.exception.InvalidWordException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final List<String> words;
    private final LogWriter logWriter;

    public WordleDictionary(List<String> words, LogWriter logWriter) {
        this.words = words;
        this.logWriter = logWriter;
    }

    public List<String> getWords() {
        return words;
    }

    public String getHintString(String userWord, String answer) {
        StringBuilder hint = new StringBuilder();
        for (int i = 0; i < userWord.length(); i++) {
            if (userWord.charAt(i) == answer.charAt(i)) {
                hint.append("+");
            } else if (answer.contains(String.valueOf(userWord.charAt(i)))) {
                hint.append("^");
            } else {
                hint.append("-");
            }
        }
        return hint.toString();
    }

    public void checkUserWord(String userWord) throws InvalidWordException, WordNotFoundInDictionaryException {
        logWriter.write("Проверка введенного игроком слова на корректность.");
        if (userWord.isBlank()) {
            throw new InvalidWordException("Введенное слово состоит из пробелов");
        }
        if (userWord.length() != 5) {
            throw new InvalidWordException("Введено слово не из 5 символов");
        }
        if (!words.contains(userWord)) {
            throw new WordNotFoundInDictionaryException("Введенное слово не найдено в словаре", userWord);
        }
    }

    public String formatWord(String word) {
        return word.toLowerCase().replace("ё", "е");
    }

    public List<String> getPossibleWords(Map<String, String> stepHistory) {
        List<String> possibleWords = new ArrayList<>();
        for (String word : words) {
            if (matchesAllHints(word, stepHistory)) {
                possibleWords.add(word);
            }
        }
        logWriter.write("Создан список слов, подходящих под все подсказки - " + possibleWords.size() + " слов.");
        return possibleWords;
    }

    private boolean matchesAllHints(String word, Map<String, String> stepHistory) {
        for (Map.Entry<String, String> entry : stepHistory.entrySet()) {
            String step = entry.getKey();
            String hint = entry.getValue();

            if (!matchesHint(word, step, hint)) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesHint(String word, String step, String hint) {
        for (int i = 0; i < hint.length(); i++) {
            char stepChar = step.charAt(i);
            char hintChar = hint.charAt(i);

            if (hintChar == '+') {
                if (word.charAt(i) != stepChar) {
                    return false;
                }
            }
            if (hintChar == '^') {
                if (!isLetterInWord(stepChar, word)) {
                    return false;

                }
            }
            if (hintChar == '-') {
                if (isLetterInWord(stepChar, word)) {
                    return false;

                }
            }
        }
        return true;
    }

    private boolean isLetterInWord(char letter, String word) {
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == letter) {
                return true;
            }
        }
        return false;
    }
}
