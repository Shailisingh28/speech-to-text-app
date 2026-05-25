package com.sttapp.Controller;

import com.sttapp.DTO.UploadResponse;
import com.sttapp.Model.Transcription;
import com.sttapp.Repository.TranscriptionRepo;
import com.sttapp.Service.FileStorageService;
import com.sttapp.Service.SpeechService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
public class SpeechController {

    private final FileStorageService fileStorageService;
    private final SpeechService speechService;
    private final TranscriptionRepo transcriptionRepo;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAudio(
            @RequestParam("file") MultipartFile file) {

        try {

            String savedFile = fileStorageService.saveFile(file);

            return ResponseEntity.ok(
                    new UploadResponse(
                            savedFile,
                            "File uploaded successfully"));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/transcribe")
    public ResponseEntity<?> transcribe(
            @RequestParam("file") MultipartFile file) {

        try {

            String savedFile = fileStorageService.saveFile(file);

            String path = "uploads/" + savedFile;

            String transcript = speechService.transcribeAudio(path);

            Transcription transcription = Transcription.builder()
                    .audioFile(savedFile)
                    .transcript(transcript)
                    .createdAt(LocalDateTime.now())
                    .build();

            transcriptionRepo.save(transcription);

            return ResponseEntity.ok(transcript);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
