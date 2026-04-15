package ru.yandex.practicum;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle{

    public static void main(String[] args) throws IOException {
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        WordleDictionary dictionary = wordleDictionaryLoader.getDictionary("words_ru.txt");
        System.out.println(dictionary.getWords());

    }



}
