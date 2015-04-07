package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.Map;

public abstract class VideoInfoBuilder {
    protected VideoInfo videoInfo;

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void createNewVideoInfo() {
        videoInfo = new VideoInfo();
    }

    public abstract void buildFilename(String filename);
    public abstract void buildCreationTime(String creationTime);
    public abstract void buildDurationString(String durationString);
    public abstract void buildDuration(Integer duration);
    public abstract void buildBit_rate(Integer bit_rate);
    public abstract void buildHeight(Integer height);
    public abstract void buildWidth(Integer width);
    public abstract void buildVideoStream(Map<String, Object> videoStream);
    public abstract void buildAudioStream(Map<String, Object> audioStream);
}