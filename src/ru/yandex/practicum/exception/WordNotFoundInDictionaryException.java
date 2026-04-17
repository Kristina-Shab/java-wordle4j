package ru.yandex.practicum.exception;

public class WordNotFoundInDictionaryException extends GameException {
    private final String inputWord;

    public WordNotFoundInDictionaryException(String message, String inputWord) {
        super(message);
        this.inputWord = inputWord;
    }

    public String getDetailMessage() {
        return getMessage() + " - " + inputWord;
    }
}
