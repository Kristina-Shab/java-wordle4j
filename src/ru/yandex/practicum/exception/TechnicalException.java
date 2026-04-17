package ru.yandex.practicum.exception;

public class TechnicalException extends RuntimeException {
    public TechnicalException(String message) {
        super(message);
    }
}
