package ru.vk.education.job.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {
    private static final Path FILE_PATH = Path.of("logs/command.log");

    public FileService() {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            if (!Files.exists(FILE_PATH))
                Files.createFile(FILE_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logCommand(String command) throws IOException {
        Files.writeString(FILE_PATH, command + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    public List<String> getCommandLog() throws IOException {
        return Files.readAllLines(FILE_PATH);
    }
}
