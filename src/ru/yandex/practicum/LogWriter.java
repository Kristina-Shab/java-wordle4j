package ru.yandex.practicum;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class LogWriter implements AutoCloseable {
    private final PrintWriter writer;

    public LogWriter() {
        try {
            Writer fileWriter = new FileWriter(createLogFile(), true);
            this.writer = new PrintWriter(fileWriter);
        } catch (IOException e) {
            throw new RuntimeException();
        }
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
        writer.println(text);
    }

    public void write(Exception e) {
        writer.println(e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
    }

    @Override
    public void close() {
        writer.close();
    }
}
