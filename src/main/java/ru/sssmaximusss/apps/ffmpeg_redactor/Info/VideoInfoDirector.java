package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.Map;

public class VideoInfoDirector {
    private VideoInfoBuilder videoInfoBuilder;

    public void setVideoInfoBuilder(VideoInfoBuilder videoInfoBuilder) {
        this.videoInfoBuilder = videoInfoBuilder;
    }

    public VideoInfo getVideoInfo() {
        return videoInfoBuilder.getVideoInfo();
    }

    public void constructVideoInfo(Map<String, Map<String, Object>> parsingInfo) {
        videoInfoBuilder.createNewVideoInfo ();
        videoInfoBuilder.buildFilename      (parsingInfo.get("format").get("filename").toString());
        videoInfoBuilder.buildCreationTime  (parsingInfo.get("format").get("creation_time").toString());
        videoInfoBuilder.buildDurationString(parsingInfo.get("format").get("duration_string").toString());
        videoInfoBuilder.buildDuration      (Integer.parseInt(parsingInfo.get("format").get("duration").toString()));
        videoInfoBuilder.buildBit_rate      (Integer.parseInt(parsingInfo.get("format").get("bit_rate").toString()));
        videoInfoBuilder.buildWidth         (Integer.parseInt(parsingInfo.get("video").get("width").toString()));
        videoInfoBuilder.buildHeight        (Integer.parseInt(parsingInfo.get("video").get("height").toString()));
        videoInfoBuilder.buildVideoStream   (parsingInfo.get("video"));
        videoInfoBuilder.buildAudioStream   (parsingInfo.get("audio"));
    }
}
