package ru.yandex.practicum;

import ru.yandex.practicum.exception.GameException;
import ru.yandex.practicum.exception.NoHintsException;

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

    public static void main(String[] args) {
        try (LogWriter logWriter = new LogWriter()) {
            WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
            WordleDictionary dictionary = wordleDictionaryLoader.getDictionary("words_ru.txt");
            WordleGame game = new WordleGame(dictionary);
            Scanner scanner = new Scanner(System.in);

            System.out.println("Игра начинается! Для выхода нажмите *");
            while (game.getSteps() > 0) {
                System.out.println("Введите слово");
                String word = scanner.nextLine();
                if (word.equals("*")) {
                    System.out.println("До свидания! Спасибо за игру.");
                    break;
                }
                if (word.isEmpty()) {
                    word = requestHint(game, scanner);
                }
                String wordValid = checkAndGetWordIfRequired(dictionary, scanner, word);
                if (wordValid.equals("*")) {
                    System.out.println("До свидания! Спасибо за игру.");
                    break;
                }
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
        } catch (RuntimeException e) {
            System.out.println("Произошла техническая неполадка. Попробуйте запустить игру заново.");
        }
    }

    private static String requestHint(WordleGame game, Scanner scanner) {
        try {
            String hintWord = game.getRandomHint();
            System.out.println(hintWord);
            return hintWord;
        } catch (NoHintsException e) {
            System.out.println("Не получилось воспользоваться подсказкой, введите слово самостоятельно");
            return scanner.nextLine();
        }
    }

    private static String checkAndGetWordIfRequired(
            WordleDictionary dictionary,
            Scanner scanner,
            String word
    ) {
        String currentWord = word;
        while (true) {
            try {
                String formatWord = dictionary.formatWord(currentWord);
                dictionary.checkUserWord(formatWord);
                return formatWord;
            } catch (GameException e) {
                System.out.println("Введите корректное слово");
                currentWord = scanner.nextLine();
                if (currentWord.equals("*")) {
                    return currentWord;
                }
            }
        }
    }
}
