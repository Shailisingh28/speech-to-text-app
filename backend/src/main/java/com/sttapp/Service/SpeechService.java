package com.sttapp.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class SpeechService {

    @Value("${assemblyai.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String transcribeAudio(String filePath)
            throws Exception {

        File audioFile = new File(filePath);

        byte[] audioBytes = Files.readAllBytes(audioFile.toPath());

        HttpHeaders uploadHeaders = new HttpHeaders();

        uploadHeaders.set("authorization", apiKey);
        uploadHeaders.setContentType(
                MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> uploadEntity = new HttpEntity<>(audioBytes,
                uploadHeaders);

        ResponseEntity<String> uploadResponse = restTemplate.exchange(
                "https://api.assemblyai.com/v2/upload",
                HttpMethod.POST,
                uploadEntity,
                String.class);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode uploadJson = mapper.readTree(
                uploadResponse.getBody());

        String audioUrl = uploadJson.get("upload_url")
                .asText();

        HttpHeaders transcriptHeaders = new HttpHeaders();

        transcriptHeaders.set(
                "authorization",
                apiKey);
        transcriptHeaders.setContentType(MediaType.APPLICATION_JSON);

        String transcriptRequest = "{ " +
                "\"audio_url\": \"" + audioUrl + "\"," +
                "\"speech_models\": [\"universal-2\"]" +
                " }";

        HttpEntity<String> transcriptEntity = new HttpEntity<>(
                transcriptRequest,
                transcriptHeaders);

        ResponseEntity<String> transcriptResponse = restTemplate.exchange(
                "https://api.assemblyai.com/v2/transcript",
                HttpMethod.POST,
                transcriptEntity,
                String.class);

        JsonNode transcriptJson = mapper.readTree(
                transcriptResponse.getBody());

        String transcriptId = transcriptJson.get("id")
                .asText();

        while (true) {

            ResponseEntity<String> pollingResponse = restTemplate.exchange(
                    "https://api.assemblyai.com/v2/transcript/"
                            + transcriptId,
                    HttpMethod.GET,
                    new HttpEntity<>(transcriptHeaders),
                    String.class);

            JsonNode pollingJson = mapper.readTree(
                    pollingResponse.getBody());

            String status = pollingJson.get("status")
                    .asText();

            if (status.equals("completed")) {

                return pollingJson.get("text")
                        .asText();
            }

            if (status.equals("error")) {

                throw new RuntimeException(
                        "Transcription failed");
            }

            Thread.sleep(3000);
        }
    }
}