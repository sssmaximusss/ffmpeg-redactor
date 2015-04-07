package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.Map;

public class CurrentVideoInfoBuilder extends VideoInfoBuilder {

    @Override
    public void buildFilename(String filename) {
        videoInfo.setFilename(filename);
    }

    @Override
    public void buildCreationTime(String creationTime) {
        videoInfo.setCreationTime(creationTime);
    }

    @Override
    public void buildDurationString(String durationString) {
        videoInfo.setDurationString(durationString);
    }

    @Override
    public void buildDuration(Integer duration) {
        videoInfo.setDuration(duration);
    }

    @Override
    public void buildBit_rate(Integer bit_rate) {
        videoInfo.setBit_rate(bit_rate);
    }

    @Override
    public void buildHeight(Integer height) {
        videoInfo.setHeight(height);
    }

    @Override
    public void buildWidth(Integer width) {
        videoInfo.setWidth(width);
    }

    @Override
    public void buildVideoStream(Map<String, Object> videoStream) {
        videoInfo.setVideoStream(videoStream);
    }

    @Override
    public void buildAudioStream(Map<String, Object> audioStream) {
        videoInfo.setAudioStream(audioStream);
    }
}

