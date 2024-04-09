package com.stibodx.demo.repository.impl;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.repository.AuthorizationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AuthorizationRepositoryImpl implements AuthorizationRepository {
    EntityManager entityManager;
    @Autowired
    public AuthorizationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public Optional<Authorities> findById(String id) {
        TypedQuery<Authorities> query = entityManager.createQuery("From Authorities where id = :id", Authorities.class);
        query.setParameter("id", id);
        //posible Nullpointer exception, trabajar con optionals
        return query.getResultList().stream().findFirst();
    }
}
