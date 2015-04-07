package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.HashMap;
import java.util.Map;


public class VideoInfo {

    private String filename = "";
    private String creationTime = "";

    private String durationString = "";
    private Integer duration = null;
    private Integer bit_rate = null;

    private Integer width = null;
    private Integer height = null;

    private Map<String, Object> videoStream = new HashMap<>();
    private Map<String, Object> audioStream = new HashMap<>();

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public void setDurationString(String durationString) {
        this.durationString = durationString;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setBit_rate(Integer bit_rate) {
        this.bit_rate = bit_rate;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setVideoStream(Map<String, Object> videoStream) {
        this.videoStream = videoStream;
    }

    public void setAudioStream(Map<String, Object> audioStream) {
        this.audioStream = audioStream;
    }

    public String getFilename() {
        return filename;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getDurationString() {
        return durationString;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getBit_rate() {
        return bit_rate;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Map<String, Object> getVideoStream() {
        return videoStream;
    }

    public  Map<String, Object> getAudioStream() {
        return audioStream;
    }


}
