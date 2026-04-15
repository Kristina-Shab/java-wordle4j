package ru.yandex.practicum;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) throws IOException {
        WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
        WordleDictionary dictionary = wordleDictionaryLoader.getDictionary("words_ru.txt");
        WordleGame game = new WordleGame(dictionary);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Игра начинается!");
        while (game.getSteps() > 0) {
            System.out.println("Введите слово");
            String word = scanner.nextLine();
            while (!dictionary.isValidUserWord(word)) {
                System.out.println("Введите корректное слово");
                word = scanner.nextLine();
            }
            String wordValid = dictionary.formatWord(word);
            if (game.isWin(wordValid)) {
                System.out.println("Поздравляем. Вы выйграли.");
                break;
            }
            System.out.println(game.gameStep(wordValid));
            System.out.println("Осталось попыток: " + game.getSteps());
        }
        if (game.getSteps() == 0) {
            System.out.println("К сожалению вы проиграли. В следующий раз все получится.");
            System.out.println("Загаданное слово - " + game.getAnswer());
        }
    }
}
