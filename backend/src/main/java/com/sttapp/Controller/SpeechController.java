package com.sttapp.Controller;

import com.sttapp.DTO.UploadResponse;
import com.sttapp.Model.Transcription;
import com.sttapp.Repository.TranscriptionRepo;
import com.sttapp.Service.FileStorageService;
import com.sttapp.Service.SpeechService;

// Lombok is not being processed for this build, so provide an explicit constructor.

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/speech")
public class SpeechController {

    private final FileStorageService fileStorageService;
    private final SpeechService speechService;
    private final TranscriptionRepo transcriptionRepo;

    public SpeechController(FileStorageService fileStorageService, SpeechService speechService,
            TranscriptionRepo transcriptionRepo) {
        this.fileStorageService = fileStorageService;
        this.speechService = speechService;
        this.transcriptionRepo = transcriptionRepo;
    }

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

            Transcription transcription = new Transcription();
            transcription.setAudioFile(savedFile);
            transcription.setTranscript(transcript);
            transcription.setCreatedAt(LocalDateTime.now());

            transcriptionRepo.save(transcription);

            return ResponseEntity.ok(java.util.Map.of("transcript", transcript));

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }
}
