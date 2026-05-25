package com.sttapp.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("No file was uploaded");
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        boolean allowedMime = contentType != null && (contentType.equals("audio/mpeg") ||
                contentType.equals("audio/mp3") ||
                contentType.equals("audio/wav") ||
                contentType.equals("audio/x-wav") ||
                contentType.equals("audio/x-pn-wav") ||
                contentType.equals("audio/x-mpeg") ||
                contentType.equals("audio/wave"));

        boolean allowedExtension = false;
        if (originalFilename != null) {
            String lowerName = originalFilename.toLowerCase();
            allowedExtension = lowerName.endsWith(".mp3") || lowerName.endsWith(".wav");
        }

        if (!allowedMime && !allowedExtension) {
            throw new RuntimeException(
                    "Only MP3 and WAV files allowed");
        }

        String fileName = System.currentTimeMillis()
                + "_" + originalFilename;

        Path path = Paths.get(UPLOAD_DIR + fileName);

        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }

        Files.copy(
                file.getInputStream(),
                path,
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}