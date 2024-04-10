package com.stibodx.demo.controller;

import com.stibodx.demo.controller.mapper.UserAuthMapper;
import com.stibodx.demo.controller.mapper.UserMapper;
import com.stibodx.demo.service.UserService;
import com.stibodx.demo.utils.JWTUtils;
import com.stibodx.demo.utils.LogInfo;
import com.stibodx.demo.utils.UserAuth;
import com.stibodx.demo.views.ErrorResponse;
import com.stibodx.demo.views.TokenViewResponse;
import com.stibodx.demo.views.UserViewRequest;
import com.stibodx.demo.views.UserViewResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final String secretKey;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder,
                          UserMapper userMapper, UserAuthMapper userAuthMapper,
                          @Value("${myapp.security.secretkey}") String secretKey) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.userAuthMapper = userAuthMapper;
        this.secretKey = secretKey;
    }

    @Operation(summary = "Sign In", description = "This service save a new user and " +
            "return a token for future requests", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenViewResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    })
    @PostMapping("/signIn")
    public TokenViewResponse saveUser(@Valid @RequestBody UserViewRequest userView) {
        LogInfo logInfo = new LogInfo(this.getClass(), userView.getName());
        logInfo.generateInfoLog("New sign in request with body: ", userView.toString());
        userView.setPassword(passwordEncoder.encode(userView.getPassword()));
        List<String> auths = Objects.isNull(userView.getAuthorities()) ? Collections.emptyList()
                : userView.getAuthorities();
        UserAuth userAuth = userAuthMapper.userEntityToUserAuth(userService
                .save(userMapper.userDtoToEntity(userView), auths, logInfo));
        String token = JWTUtils.generateToken(userAuth, secretKey);
        TokenViewResponse viewResponse = new TokenViewResponse(token, userAuth.getUsername());
        logInfo.generateInfoLog("Sign in request ended with response: ", viewResponse.toString());
        return viewResponse;
    }
    @Operation(summary = "Log In", description = "This service get a user check his existence" +
            " in DB and if exists also return a token for future requests", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenViewResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    }, security = {
            @SecurityRequirement(name = "basicAuth")
    })
    @GetMapping("/logIn")
    public TokenViewResponse logIn(@AuthenticationPrincipal UserAuth user) {
        LogInfo logInfo = new LogInfo(this.getClass(), user.getUsername());
        logInfo.generateInfoLog("New log in request with body: ", user.toString());
        String token = JWTUtils.generateToken(user, secretKey);
        TokenViewResponse response = new TokenViewResponse(token, user.getUsername());
        logInfo.generateInfoLog("Log in request ended with response: ", response.toString());
        return response;
    }
    @Operation(summary = "Get info by user name", description = "This service get a specific user name check his existence" +
            " in DB and return his info", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenViewResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    }, security = {
            @SecurityRequirement(name = "Authorization")
    })
    @GetMapping("/getUserByName")
    public UserViewResponse getUserByName(@AuthenticationPrincipal UserAuth user,
                                          @RequestParam(name = "userName") String userName) {
        LogInfo logInfo = new LogInfo(this.getClass(), user.getUsername());
        logInfo.generateInfoLog("New get userByName request and looking for: ", userName);
        UserViewResponse userViewResponse = userMapper.userEntityToDto(userService.findByName(userName, user.getAuthoritiesIds(), logInfo));
        logInfo.generateInfoLog("getUserName request ended with response: ", userViewResponse.toString());
        return userViewResponse;
    }
    @Operation(summary = "Get info by user name like", description = "This service get a part of a user name and check " +
            "if in DB there is a user with a name like the provided in this case it also return his info", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenViewResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    }, security = {
            @SecurityRequirement(name = "Authorization")
    })
    @GetMapping("/getUserByNameLike")
    public UserViewResponse getUserByNameLike(@AuthenticationPrincipal UserAuth user,
                                              @RequestParam(name = "userName") String userName) {
        LogInfo logInfo = new LogInfo(this.getClass(), user.getUsername());
        logInfo.generateInfoLog("New getUserByNameLike request and looking for a user like: ", userName);
        UserViewResponse userViewResponse = userMapper.userEntityToDto(userService.findByNameLike(userName, user.getAuthoritiesIds(), logInfo));
        logInfo.generateInfoLog("getUserByNameLike request ended with response: ", userViewResponse.toString());
        return userViewResponse;
    }
    @Operation(summary = "Get all users", description = "This service returns all user from DB", responses = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TokenViewResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            }),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    }, security = {
            @SecurityRequirement(name = "Authorization")
    })
    @GetMapping("/getAllUser")
    public List<UserViewResponse> getAllUser(@AuthenticationPrincipal UserAuth user) {
        LogInfo logInfo = new LogInfo(this.getClass(), user.getUsername());
        logInfo.generateInfoLog("New getAllUser request");
        List<UserViewResponse> userViewResponse = userService.findByAllUsers(user.getAuthoritiesIds(), logInfo).stream().map(userMapper::userEntityToDto).toList();
        logInfo.generateInfoLog("getUserByNameLike request ended with response: ", userViewResponse.toString());
        return userViewResponse;
    }
}
