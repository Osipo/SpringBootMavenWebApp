package org.example.utils;

import org.springframework.http.MediaType;

import java.util.List;

public interface IStringToMediaTypeConverter {
    List<MediaType> getMedias(String key, MediaType def);
    MediaType getFirstByQualityOrDefault(String key, MediaType def);
}
