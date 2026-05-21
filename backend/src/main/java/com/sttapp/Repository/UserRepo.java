package com.sttapp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sttapp.Model.Users;

public interface UserRepo extends JpaRepository<Users, Long> {
    public Optional<Users> findByEmail(String email);
}
