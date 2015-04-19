package ru.sssmaximusss.apps.ffmpeg_redactor.Info;

import java.util.Map;

public class VideoInfoDirector {
    private VideoInfoBuilder videoInfoBuilder;

    public VideoInfoDirector() {
        videoInfoBuilder = new VideoInfoBuilder();
    }

    public VideoInfo constructVideoInfo(Map<String, Map<String, Object>> parsingInfo) {
        videoInfoBuilder.withFilename(parsingInfo.get("format").get("filename").toString())
                        .withCreationTime(parsingInfo.get("format").get("creation_time").toString())
                        .withDurationString(parsingInfo.get("format").get("duration_string").toString())
                        .withDuration(Integer.parseInt(parsingInfo.get("format").get("duration").toString()))
                        .withBit_rate(Integer.parseInt(parsingInfo.get("format").get("bit_rate").toString()))
                        .withWidth(Integer.parseInt(parsingInfo.get("video").get("width").toString()))
                        .withHeight(Integer.parseInt(parsingInfo.get("video").get("height").toString()))
                        .withVideoStream(parsingInfo.get("video"))
                        .withAudioStream(parsingInfo.get("audio"));
        return videoInfoBuilder.build();
    }
}
