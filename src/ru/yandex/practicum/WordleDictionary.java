package ru.yandex.practicum;

import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
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

    public boolean isValidUserWord(String userWord) {
        if (userWord.isBlank()) {
            return false;
        }
        userWord = userWord.trim();
        if (userWord.length() != 5) {
            return false;
        }
        return words.contains(userWord);
    }

    public String formatWord(String word) {
        return word.toLowerCase().replace("ё", "е");
    }
}
