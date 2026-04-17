package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryLoadException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary getDictionary(String nameFile) {
        List<String> validWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nameFile, StandardCharsets.UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (line == null) {
                    throw new DictionaryLoadException("При чтении словаря из файла обнаружена null-строка");
                }
                addWordsToDictionary(line, validWords);
            }
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка чтения файла: " + nameFile);
        }
        return new WordleDictionary(validWords);
    }

    private void addWordsToDictionary(String word, List<String> validWords) {
        if (isValidWord(word)) {
            word = word.trim().toLowerCase().replace("ё", "е");
            validWords.add(word);
        }
    }

    private boolean isValidWord(String word) {
        if (word == null) {
            return false;
        }
        return word.trim().length() == 5;
    }

}
