package com.stibodx.demo.repository;

import com.stibodx.demo.entities.Authorities;

import java.util.Optional;

public interface AuthorizationRepository {
    abstract Optional<Authorities> findById(String id);
}
