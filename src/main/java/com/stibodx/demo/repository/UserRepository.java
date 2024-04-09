package com.stibodx.demo.repository;

import com.stibodx.demo.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    abstract void save(User user);
    abstract Optional<User> findByName(String name);
    abstract User findById(String id);
    abstract Optional<User> findByNameLike(String name);
    abstract List<User> findAllUsers();
    abstract Optional<User> userAlredyExists(String name, String email, String dni);
}
