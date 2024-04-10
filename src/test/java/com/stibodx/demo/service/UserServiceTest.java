package com.stibodx.demo.service;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.exceptions.AuthNoFoundException;
import com.stibodx.demo.exceptions.ServiceException;
import com.stibodx.demo.exceptions.UserAlredyExistsException;
import com.stibodx.demo.repository.AuthorizationRepository;
import com.stibodx.demo.repository.UserRepository;
import com.stibodx.demo.service.impl.UserServiceImpl;
import com.stibodx.demo.utils.LogInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;
    private final UserService userService;
    private final String TEST_VARIALBE;

    public UserServiceTest() {
        userRepository = Mockito.mock(UserRepository.class);
        authorizationRepository = Mockito.mock(AuthorizationRepository.class);
        userService = new UserServiceImpl(userRepository, authorizationRepository);
        TEST_VARIALBE = "test";
    }

    @Test
    public void saveUserAlredyExistingException() {
        User user = new User();
        user.setName(TEST_VARIALBE);
        user.setEmail(TEST_VARIALBE);
        user.setDni(TEST_VARIALBE);
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(user));
        assertThrows(UserAlredyExistsException.class,
                () -> userService.save(user, Collections.emptyList(), new LogInfo(UserServiceTest.class, "testUser")));
    }
    @Test
    public void saveUserAuthNotFound() {
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(authorizationRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        User user = new User();
        assertThrows(AuthNoFoundException.class,
                () -> userService.save(user, Collections.emptyList(), new LogInfo(UserServiceTest.class, "testUser")));
    }
    @Test
    public void saveUserRoleNotFound() {
        User user = new User();
        user.setName(TEST_VARIALBE);
        user.setEmail(TEST_VARIALBE);
        user.setDni(TEST_VARIALBE);
        Authorities auths = new Authorities(1, "ROLE_normal", null);
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(authorizationRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(auths));

        User userResponse = userService.save(user, Collections.singletonList("2"), new LogInfo(UserServiceTest.class, "testUser"));
//        user = new User();
//        user.setName(TEST_VARIALBE);
//        user.setEmail(TEST_VARIALBE);
//        user.setDni(TEST_VARIALBE);
//        user.setAuthorities(Collections.singletonList(auths));
//        user.setSignUpDate(LocalDate.now());
        //Service returns same instance that it takes
        assertEquals(user, userResponse);
    }

    @Test
    public void saveUserRoleExists() {
        User user = new User();
        user.setName(TEST_VARIALBE);
        user.setEmail(TEST_VARIALBE);
        user.setDni(TEST_VARIALBE);
        Authorities auths = new Authorities(1, "ROLE_normal", null);
        user.setAuthorities(Collections.singletonList(auths));
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(authorizationRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(auths));

        User userResponse = userService.save(user, Collections.singletonList("1"), new LogInfo(UserServiceTest.class, "testUser"));
        assertEquals(user, userResponse);
    }
    @Test
    public void saveUserNoSpecificRole() {
        User user = new User();
        user.setName(TEST_VARIALBE);
        user.setEmail(TEST_VARIALBE);
        user.setDni(TEST_VARIALBE);
        Authorities auths = new Authorities(1, "ROLE_normal", null);
        user.setAuthorities(Collections.singletonList(auths));
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito.when(authorizationRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(auths));

        User userResponse = userService.save(user, Collections.emptyList(), new LogInfo(UserServiceTest.class, "testUser"));
        assertEquals(user, userResponse);
        assertEquals(userResponse.getAuthorities().get(0).getRole(), "ROLE_normal");
    }
    @Test
    public void saveUserServiceException() {
        User user = new User();
        user.setName(TEST_VARIALBE);
        user.setEmail(TEST_VARIALBE);
        user.setDni(TEST_VARIALBE);
        Mockito.when(userRepository.userAlredyExists(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        assertThrows(ServiceException.class,
                () -> userService.save(user, null, new LogInfo(UserServiceTest.class, "testUser")));
    }
}
