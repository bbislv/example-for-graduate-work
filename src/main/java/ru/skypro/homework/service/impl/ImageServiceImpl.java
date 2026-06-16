package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.constant.ImageConstants;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exception.InvalidImageException;
import ru.skypro.homework.exception.ResourceNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_NOT_FOUND = "Изображение не найдено";
    private static final String EMPTY_IMAGE = "Файл изображения не передан";
    private static final String INVALID_IMAGE_TYPE = "Допустимы только файлы изображений";
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp", ".bmp");

    private final ImageRepository imageRepository;
    private final Path uploadDirectory;

    public ImageServiceImpl(
            ImageRepository imageRepository,
            @Value("${app.images.upload-dir}") String uploadDir) {
        this.imageRepository = imageRepository;
        this.uploadDirectory = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void initUploadDirectory() throws IOException {
        Files.createDirectories(uploadDirectory);
    }

    @Override
    @Transactional
    public String uploadImage(MultipartFile image) {
        validateImage(image);
        String imageId = UUID.randomUUID().toString();
        String extension = resolveExtension(image);
        String fileName = imageId + extension;
        Path targetPath = uploadDirectory.resolve(fileName);

        try (InputStream inputStream = image.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            log.error("Не удалось сохранить изображение в {}", targetPath, exception);
            throw new InvalidImageException("Не удалось сохранить изображение");
        }

        Image imageEntity = new Image();
        imageEntity.setId(imageId);
        imageEntity.setFileName(fileName);
        imageEntity.setMediaType(resolveMediaType(image, extension));
        imageRepository.save(imageEntity);

        return ImageConstants.URL_PREFIX + imageId;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageBytes(String imageId) {
        Image image = getImageEntity(imageId);
        Path filePath = uploadDirectory.resolve(image.getFileName());
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException(IMAGE_NOT_FOUND);
        }
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException exception) {
            throw new ResourceNotFoundException(IMAGE_NOT_FOUND);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MediaType getMediaType(String imageId) {
        Image image = getImageEntity(imageId);
        return MediaType.parseMediaType(image.getMediaType());
    }

    @Override
    @Transactional
    public void deleteImageByPath(String imagePath) {
        extractImageId(imagePath).ifPresent(this::deleteImage);
    }

    private void deleteImage(String imageId) {
        imageRepository.findById(imageId).ifPresent(image -> {
            Path filePath = uploadDirectory.resolve(image.getFileName());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException exception) {
                log.warn("Не удалось удалить файл изображения {}", filePath, exception);
            }
            imageRepository.delete(image);
        });
    }

    private Image getImageEntity(String imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(IMAGE_NOT_FOUND));
    }

    private Optional<String> extractImageId(String imagePath) {
        if (imagePath == null || !imagePath.startsWith(ImageConstants.URL_PREFIX)) {
            return Optional.empty();
        }
        return Optional.of(imagePath.substring(ImageConstants.URL_PREFIX.length()));
    }

    private void validateImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new InvalidImageException(EMPTY_IMAGE);
        }
        if (!isImageFile(image)) {
            throw new InvalidImageException(INVALID_IMAGE_TYPE);
        }
    }

    private boolean isImageFile(MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType != null
                && contentType.toLowerCase(Locale.ROOT).startsWith(ImageConstants.IMAGE_MEDIA_TYPE_PREFIX)) {
            return true;
        }
        return hasAllowedExtension(image.getOriginalFilename());
    }

    private boolean hasAllowedExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return false;
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.')).toLowerCase(Locale.ROOT);
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    private String resolveExtension(MultipartFile image) {
        if (hasAllowedExtension(image.getOriginalFilename())) {
            return image.getOriginalFilename()
                    .substring(image.getOriginalFilename().lastIndexOf('.'))
                    .toLowerCase(Locale.ROOT);
        }
        return switch (Optional.ofNullable(image.getContentType()).orElse("")) {
            case "image/png" -> ".png";
            case "image/gif" -> ".gif";
            case "image/webp" -> ".webp";
            case "image/bmp" -> ".bmp";
            default -> ".jpg";
        };
    }

    private String resolveMediaType(MultipartFile image, String extension) {
        String contentType = image.getContentType();
        if (contentType != null && contentType.startsWith(ImageConstants.IMAGE_MEDIA_TYPE_PREFIX)) {
            return contentType;
        }
        return switch (extension) {
            case ".png" -> MediaType.IMAGE_PNG_VALUE;
            case ".gif" -> MediaType.IMAGE_GIF_VALUE;
            case ".webp" -> "image/webp";
            case ".bmp" -> "image/bmp";
            default -> MediaType.IMAGE_JPEG_VALUE;
        };
    }
}
