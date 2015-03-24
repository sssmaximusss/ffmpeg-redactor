package ru.sssmaximusss.apps.ffmpeg_redactor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
                //Files.delete(dir);

                Files.walkFileTree(dir, new SimpleFileVisitor<Path>(){
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
