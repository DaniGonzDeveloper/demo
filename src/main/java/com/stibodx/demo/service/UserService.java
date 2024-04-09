package com.stibodx.demo.service;


import com.stibodx.demo.entities.User;
import com.stibodx.demo.utils.LogInfo;
import com.stibodx.demo.views.UserViewRequest;
import com.stibodx.demo.views.UserViewResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {

    abstract User save(User user, List<String> authsDto, LogInfo logInfo);
//    abstract UserViewResponse findByName(String name);
    abstract User findByName(String name, List<Integer> roles, LogInfo logInfo);
    abstract User findByNameLike(String name, List<Integer> roles, LogInfo logInfo);
    abstract List<User> findByAllUsers(List<Integer> roles, LogInfo logInfo);
    abstract User findByNameNoRoles(String name);
}
