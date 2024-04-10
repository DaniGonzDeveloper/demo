package com.stibodx.demo.repository;

import com.stibodx.demo.entities.User;
import com.stibodx.demo.repository.impl.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private final EntityManager entityManager;
    private final UserRepository userRepo;
    private final String TEST_VARIABLE;
    public UserRepositoryTest() {
        entityManager = Mockito.mock(EntityManager.class);
        userRepo = new UserRepositoryImpl(entityManager);
        TEST_VARIABLE = "test";
    }

    @Test
    public void userAlredyExistsThrowsRunTimeException() {
        Mockito.when(entityManager.createQuery(Mockito.anyString(),Mockito.any()))
                .thenThrow(NullPointerException.class);
        assertThrows(RuntimeException.class,
                () -> userRepo.userAlredyExists(TEST_VARIABLE,TEST_VARIABLE,TEST_VARIABLE));
    }
    @Test
    public void userAlredyExistsReturnValue() {
        User user = new User();
        user.setEmail(TEST_VARIABLE);
        TypedQuery<User> mockedQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(User.class))).thenReturn(mockedQuery);
        Mockito.doReturn(null).when(mockedQuery).setParameter(Mockito.anyString(), Mockito.any());
        Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.singletonList(user));
        Optional<User> userOptional = userRepo.userAlredyExists(TEST_VARIABLE,TEST_VARIABLE,TEST_VARIABLE);
        assertTrue(userOptional.isPresent());
        User userResponse = userOptional.get();
        assertEquals(TEST_VARIABLE, userResponse.getEmail());
    }
    @Test
    public void userAlredyEmptyValue() {
        User user = new User();
        user.setEmail(TEST_VARIABLE);
        TypedQuery<User> mockedQuery = Mockito.mock(TypedQuery.class);
        Mockito.when(entityManager.createQuery(Mockito.anyString(), Mockito.eq(User.class))).thenReturn(mockedQuery);
        Mockito.doReturn(null).when(mockedQuery).setParameter(Mockito.anyString(), Mockito.any());
        Mockito.when(mockedQuery.getResultList()).thenReturn(Collections.emptyList());
        Optional<User> userOptional = userRepo.userAlredyExists(TEST_VARIABLE,TEST_VARIABLE,TEST_VARIABLE);
        assertTrue(userOptional.isEmpty());
    }
}
