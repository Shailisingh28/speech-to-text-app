package com.sttapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sttapp.Model.Transcription;

public interface TranscriptionRepo extends JpaRepository<Transcription, Long> {

}
