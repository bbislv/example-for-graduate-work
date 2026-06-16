package ru.skypro.homework.service;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile image);

    byte[] getImageBytes(String imageId);

    MediaType getMediaType(String imageId);

    void deleteImageByPath(String imagePath);
}
