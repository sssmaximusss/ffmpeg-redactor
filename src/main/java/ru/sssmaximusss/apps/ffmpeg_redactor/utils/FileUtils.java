package ru.sssmaximusss.apps.ffmpeg_redactor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * Created by Mike on 23-Mar-15.
 */
public class FileUtils {

    public void withTempFile(Consumer<File> fileConsumer) {
        File file = null;

        try {

            file = File.createTempFile("video-editor-", "");
            fileConsumer.accept(file);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }
    }

    public void withTempDir(Consumer<Path> pathConsumer) {
        Path dir = null;

        try {

            dir = Files.createTempDirectory("video-editor-");
            pathConsumer.accept(dir);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Files.delete(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
