package ru.sssmaximusss.apps.ffmpeg_redactor;

import ru.sssmaximusss.apps.ffmpeg_redactor.Redactor;
import ru.sssmaximusss.apps.ffmpeg_redactor.RedactorImpl;
import ru.sssmaximusss.apps.ffmpeg_redactor.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Mike on 26-Mar-15.
 */
public class SlideshowMakers {

    private static final String tempImgFilePattern = "%05d.png";

    private Redactor redactor;


    public void createSimpleSlideshow (Iterable<File> inputFiles, File outputFile, String width, String height, String duration) throws IOException {

        FileUtils fileUtils = new FileUtils();
        fileUtils.withTempDir( tempDir -> {

            redactor = new RedactorImpl(tempDir.toFile());
            LinkedList<File> tempFiles = new LinkedList<File>();

            int fileNumber = 0;
            for (File inputFile : inputFiles) {
                File tempFile = new File(tempDir.toString() + File.separator + String.format(tempImgFilePattern, fileNumber++));
                try {
                    redactor.resize(inputFile, tempFile, Integer.valueOf(width), Integer.valueOf(height));
                    tempFiles.add(tempFile);
                } catch (IOException e) {
                }
            }

            try {
                redactor.imageSetToVideo(tempImgFilePattern, outputFile, Integer.parseInt(duration));
            } catch (IOException e) {
            }

        });
    }
}
