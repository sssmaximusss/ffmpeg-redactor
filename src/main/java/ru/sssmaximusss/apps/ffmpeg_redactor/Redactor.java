package ru.sssmaximusss.apps.ffmpeg_redactor;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public interface Redactor {

    public static final String DEFAULT_CMD_EXECUTE = "ffmpeg";

    public static final String DEFAULT_CMD_GETINFO = "ffprobe";

    public String extract(File file) throws IOException;

    public VideoInfo getInfo(File inputFile) throws IOException, JSONException;

    //ffmpeg -i inputFile -ss 00:01:00 -to 00:02:00
    public String cut(File inputFile, File outputFile, String start, String end) throws IOException;

    //ffmpeg -i inputFile -ss 00:01:00 -t 60
    public String cut(File inputFile, File outputFile, String start, Integer duration) throws IOException;
}
