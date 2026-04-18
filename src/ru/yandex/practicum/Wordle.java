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
        try (LogWriter logWriter = new LogWriter();
             Scanner scanner = new Scanner(System.in)) {
            WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader(logWriter);
            WordleDictionary dictionary = wordleDictionaryLoader.getDictionary("words_ru.txt");
            WordleGame game = new WordleGame(dictionary, logWriter);

            runGame(game, dictionary, scanner, logWriter);
        } catch (RuntimeException e) {
            System.out.println("Произошла техническая неполадка. Попробуйте запустить игру заново.");
        }
    }

    private static void runGame(
            WordleGame game,
            WordleDictionary dictionary,
            Scanner scanner,
            LogWriter logWriter
    ) {
        System.out.println("Игра начинается! Для запроса подсказки нажмите Enter, для выхода *");
        logWriter.write("----\nНачало игры");
        while (game.getSteps() > 0) {
            System.out.println("Введите слово");
            String word = scanner.nextLine();
            if (isExitGame(logWriter, word)) break;
            String wordValid = checkAndGetWordIfRequired(dictionary, scanner, word, logWriter, game);
            if (isExitGame(logWriter, wordValid)) break;
            if (isWinGame(game, logWriter, wordValid)) break;
            System.out.println(game.makeStep(wordValid));
            System.out.println("Осталось попыток: " + game.getSteps());
        }
        showLoseMessage(game, logWriter);
    }

    private static void showLoseMessage(WordleGame game, LogWriter logWriter) {
        if (game.getSteps() == 0) {
            logWriter.write("Ходов не осталось. Конец игры.");
            System.out.println("К сожалению вы проиграли. В следующий раз все получится.");
            System.out.println("Загаданное слово - " + game.getAnswer());
        }
    }

    private static boolean isWinGame(WordleGame game, LogWriter logWriter, String wordValid) {
        if (game.isWin(wordValid)) {
            logWriter.write("Слово угадано. Победа.");
            System.out.println("Поздравляем. Вы выиграли.");
            return true;
        }
        return false;
    }

    private static boolean isExitGame(LogWriter logWriter, String word) {
        if (word.equals("*")) {
            logWriter.write("Игрок вышел из игры.");
            System.out.println("До свидания! Спасибо за игру.");
            return true;
        }
        return false;
    }

    private static String requestHint(WordleGame game, Scanner scanner, LogWriter logWriter) {
        try {
            String hintWord = game.getRandomHint();
            System.out.println(hintWord);
            return hintWord;
        } catch (NoHintsException e) {
            logWriter.write(e);
            System.out.println("Не получилось воспользоваться подсказкой, введите слово самостоятельно");
            return scanner.nextLine();
        }
    }

    private static String checkAndGetWordIfRequired(
            WordleDictionary dictionary,
            Scanner scanner,
            String word,
            LogWriter logWriter,
            WordleGame game
    ) {
        String currentWord = word;
        while (true) {
            try {
                if (currentWord.isEmpty()) {
                    return requestHint(game, scanner, logWriter);
                }
                String formatWord = dictionary.formatWord(currentWord);
                dictionary.checkUserWord(formatWord);
                return formatWord;
            } catch (GameException e) {
                logWriter.write(e);
                System.out.println("Введите корректное слово");
                currentWord = scanner.nextLine();
                if (currentWord.equals("*")) {
                    return currentWord;
                }
            }
        }
    }
}
