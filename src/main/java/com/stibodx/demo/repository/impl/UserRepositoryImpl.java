package com.stibodx.demo.repository.impl;

import com.stibodx.demo.entities.User;
import com.stibodx.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final EntityManager entityManager;
    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    @Transactional()
    public void save(User user) {
        entityManager.persist(user);
    }
    public Optional<User> findByNameLike(String name) {
        TypedQuery<User> query = entityManager.createQuery("From User where name like :theData", User.class);
        query.setParameter("theData", "%" + name + "%");
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<User> findAllUsers() {
        TypedQuery<User> query = entityManager.createQuery("From User", User.class);
        return query.getResultList();
    }
    @Override
    public Optional<User> userAlredyExists(String name, String email, String dni) {
        TypedQuery<User> query = entityManager.createQuery("From User where name = :name OR email  = :email OR dni = :dni", User.class);
        query.setParameter("name", name);
        query.setParameter("email", email);
        query.setParameter("dni", dni);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        TypedQuery<User> query = entityManager.createQuery("From User where name = :theData", User.class);
        query.setParameter("theData", name);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public User findById(String id) {
        TypedQuery<User> query = entityManager.createQuery("From User where id = :theData", User.class);
        query.setParameter("theData", id);
        return query.getResultList().stream().findFirst().orElse(null);
    }
}