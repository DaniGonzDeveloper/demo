package com.stibodx.demo.service.impl;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.exceptions.AuthNoFoundException;
import com.stibodx.demo.exceptions.ServiceException;
import com.stibodx.demo.exceptions.UserAlredyExistsException;
import com.stibodx.demo.exceptions.UserNoFoundException;
import com.stibodx.demo.repository.AuthorizationRepository;
import com.stibodx.demo.repository.UserRepository;
import com.stibodx.demo.service.UserService;
import com.stibodx.demo.utils.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorizationRepository authorizationRepository;
    private static final String ROLE_NORMAL_ID = "1";
    private static final String ROLE_ADMIN_ID = "2";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorizationRepository authRepository) {
        this.userRepository = userRepository;
        this.authorizationRepository = authRepository;
    }

    @Override
    public User save(User user, List<String> authsDto, LogInfo logInfo) {
        logInfo = new LogInfo(this.getClass(), logInfo);
        try {
            logInfo.generateInfoLog("saving user: ", user.toString());
            user.setSignUpDate(LocalDate.now());
            List<Authorities> authorities;

            boolean userAlredyAdded = userRepository.userAlredyExists(user.getName(), user.getEmail(), user.getDni()).isPresent();
            if(userAlredyAdded) {
                throw new UserAlredyExistsException("user name, dni or email is alredy in use for another user");
            }

            authorities = authsDto.stream().map(roleId -> authorizationRepository.findById(roleId)
                    .orElseThrow(() -> new AuthNoFoundException("The roleId: " + roleId + " was not found"))).toList();
            if (ObjectUtils.isEmpty(authorities)) {
                authorities = Collections.singletonList(authorizationRepository.findById(ROLE_NORMAL_ID)
                        .orElseThrow(() -> new AuthNoFoundException("The roleId: " + ROLE_NORMAL_ID + " was not found")));
            }
            logInfo.generateInfoLog("user will have the next" +
                    " authorizations: ", authorities.toString());
            user.setAuthorities(authorities);
            userRepository.save(user);
            return user;
        } catch (UserAlredyExistsException ex) {
            logInfo.generateErrorLog("user name, dni or email is alredy in use for another user");
            throw ex;
        }catch (AuthNoFoundException ex) {
            logInfo.generateErrorLog("Error while saving user", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logInfo.generateErrorLog("Error while saving user", ex.getMessage());
            throw new ServiceException("Something went wrong while creating the user");
        }
    }

    @Override
    public User findByNameLike(String name, List<Integer> roles, LogInfo logInfo) {
        logInfo = new LogInfo(this.getClass(), logInfo);
        try {
            User user = userRepository.findByNameLike(name).orElseThrow(() -> new UserNoFoundException("The user was not found"));
            checkAuth(roles, user);
            return user;
        } catch (UserNoFoundException ex) {
            logInfo.generateErrorLog("Error getting user with name like: ", name, "caused by: ", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logInfo.generateErrorLog("Error getting user with name like: ", name, ex.getMessage());
            throw new ServiceException("Something went wrong while " +
                    "getting info about the user");
        }
    }

    @Override
    public List<User> findByAllUsers(List<Integer> roles, LogInfo logInfo) {
        try {
            return userRepository.findAllUsers().stream().map(user -> {
                checkAuth(roles, user);
                return user;
            }).toList();
        } catch (Exception ex) {
            logInfo.generateErrorLog("Error getting all users caused by: ", ex.getMessage());
            throw new ServiceException("Something went wrong while " +
                    "getting info about the user");
        }
    }

    @Override
    public User findByNameNoRoles(String name) {
        try {
            return userRepository.findByName(name).orElse(null);
        } catch (Exception ex) {
            throw new ServiceException("Something went wrong while " +
                    "getting info about the user");
        }
    }

    @Override
    public User findByName(String name, List<Integer> roles, LogInfo logInfo) {
        try {
            User user = userRepository.findByName(name).orElseThrow(() -> new UserNoFoundException("The user was not found"));
            checkAuth(roles, user);
            return user;
        } catch (UserNoFoundException ex) {
            logInfo.generateErrorLog("Error getting user with name: ", name, " caused by: ",ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            logInfo.generateErrorLog("Error getting user with name: ", name, " caused by: ",ex.getMessage());
            throw new ServiceException("Something went wrong while " +
                    "getting info about the user");
        }
    }

    private void checkAuth(List<Integer> roles, User user) {
        if (roles.stream()
                .noneMatch(authorityId -> authorityId == Integer.parseInt(ROLE_ADMIN_ID))) {
            user.setDni(null);
            user.setPhoneNumber(null);
            user.setStreet(null);
        }
    }
}
