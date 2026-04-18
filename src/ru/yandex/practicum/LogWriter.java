package ru.yandex.practicum;

import ru.yandex.practicum.exception.LogFileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class LogWriter implements AutoCloseable {
    private final PrintWriter writer;

    public LogWriter() throws LogFileException {
        try {
            Writer fileWriter = new FileWriter(createLogFile(), true);
            this.writer = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new LogFileException("Ошибка создания лог-файла");
        }
    }

    public LogWriter(PrintWriter writer) {
        this.writer = writer;
    }

    private File createLogFile() throws IOException {
        Path pathDir = Paths.get("logs");
        createDirectoryIfNotExists(pathDir);
        Path pathFile = pathDir.resolve("Logs-File.txt");
        Path path = createFileIfNotExists(pathFile);
        return path.toFile();
    }

    private void createDirectoryIfNotExists(Path pathDir) throws IOException {
        if (!Files.exists(pathDir)) {
            Files.createDirectory(pathDir);
        }
    }

    private Path createFileIfNotExists(Path pathFile) throws IOException {
        if (!Files.exists(pathFile)) {
            Files.createFile(pathFile);
        }
        return pathFile;
    }

    public void write(String text) {
        checkLogFile();
        writer.println(text);
        writer.flush();
    }

    public void write(Exception e) {
        checkLogFile();
        writer.println("Поймано исключение:");
        writer.println(e.getMessage());
        writer.println(Arrays.toString(e.getStackTrace()));
        writer.flush();
    }

    private void checkLogFile() throws LogFileException {
        Path pathFile = Paths.get("logs", "Logs-File.txt");
        if (!Files.exists(pathFile)) {
            throw new LogFileException("Лог-файл не найден или был удален");
        }
    }

    @Override
    public void close() {
        writer.close();
    }
}
