package ru.sssmaximusss.apps.ffmpeg_redactor;

import ru.sssmaximusss.apps.ffmpeg_redactor.Info.VideoInfo;

import java.io.File;
import java.io.IOException;

public interface Redactor {

    public static final String DEFAULT_CMD_EXECUTE = "ffmpeg";

    public static final String DEFAULT_CMD_GETINFO = "ffprobe";

    public String extract(final File file) throws IOException;

    public VideoInfo getInfo(final File inputFile) throws IOException, ClassNotFoundException;

    //ffmpeg -i inputFile -ss 00:01:00 -to 00:02:00
    public String cut(final File inputFile,final File outputFile, final String start, final String end) throws IOException;

    //ffmpeg -i inputFile -ss 00:01:00 -t 60
    public String cut(final File inputFile,final File outputFile, final String start, final Integer duration) throws IOException;

    public void resize(final File inputFile, final File outputFile, final int width, final int height) throws IOException;

    public void resize(final File inputFile, final String outputFile, final int width, final int height) throws IOException;

    public void imageSetToVideo(final String inputFilePattern, final File outputFile, final Integer duration) throws IOException;

    public void imageToVideo(final File inputFile, final File outputFile, final Integer duration) throws IOException;

    public void concatenate(final File inputFileList, final File outputFile) throws IOException;

    public void stabilizeStep1(final File inputFile) throws IOException;

    public void stabilizeStep2(final File inputFile, final File outputFile) throws IOException;
}
