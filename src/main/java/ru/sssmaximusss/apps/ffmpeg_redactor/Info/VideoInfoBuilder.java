package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.Map;

public class VideoInfoBuilder {
    protected VideoInfo videoInfo = new VideoInfo();

    public VideoInfo build() {
        return videoInfo;
    }

    public VideoInfoBuilder withFilename(String filename) {
        videoInfo.setFilename(filename);
        return this;
    }

    public VideoInfoBuilder withCreationTime(String creationTime) {
        videoInfo.setCreationTime(creationTime);
        return this;
    }

    public VideoInfoBuilder withDurationString(String durationString) {
        videoInfo.setDurationString(durationString);
        return this;
    }

    public VideoInfoBuilder withDuration(Integer duration) {
        videoInfo.setDuration(duration);
        return this;
    }

    public VideoInfoBuilder withBit_rate(Integer bit_rate) {
        videoInfo.setBit_rate(bit_rate);
        return this;
    }

    public VideoInfoBuilder withHeight(Integer height) {
        videoInfo.setHeight(height);
        return this;
    }

    public VideoInfoBuilder withWidth(Integer width) {
        videoInfo.setWidth(width);
        return this;
    }

    public VideoInfoBuilder withVideoStream(Map<String, Object> videoStream) {
        videoInfo.setVideoStream(videoStream);
        return this;
    }

    public VideoInfoBuilder withAudioStream(Map<String, Object> audioStream) {
        videoInfo.setAudioStream(audioStream);
        return this;
    }
}