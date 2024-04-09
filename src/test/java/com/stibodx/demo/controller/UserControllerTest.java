package com.stibodx.demo.controller;

import com.stibodx.demo.config.WithMockCustomUser;
import com.stibodx.demo.controller.mapper.UserAuthMapper;
import com.stibodx.demo.controller.mapper.UserMapper;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.service.UserService;
import com.stibodx.demo.views.UserViewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserAuthMapper userAuthMapper;
    private String secretKey;

    @BeforeEach
    public void setUp() {
        secretKey = "test";
    }

    @Test
    @WithMockCustomUser
    public void findByNameOk() throws Exception {
        String userNameInDB = "testUserDb";
        String userNameDto = "testUserDto";
        User user = new User();
        user.setName(userNameInDB);
        ArgumentCaptor<String> userNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(userService.findByName(userNameArgumentCaptor.capture(), Mockito.any(), Mockito.any()))
                .thenReturn(user);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.when(userMapper.userEntityToDto(userArgumentCaptor.capture()))
                .thenReturn(UserViewResponse.builder()
                        .name(userNameDto)
                        .build());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getUserByName")
                        .param("userName", "test"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(userNameDto));
        String userName = userNameArgumentCaptor.getValue();
        User userCaptureValue = userArgumentCaptor.getValue();
        assertEquals(userName, "test");
        assertEquals(userCaptureValue.getName(), userNameInDB);
    }

    @Test
    @WithMockCustomUser
    public void findByNameNoNameParam() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getUserByName"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(400));
    }

    @Test
    @WithMockCustomUser
    public void findByNameServiceThrowsException() throws Exception {
        String userNameInDB = "testUserDb";
        User user = new User();
        user.setName(userNameInDB);
        ArgumentCaptor<String> userNameArgumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.when(userService.findByName(userNameArgumentCaptor.capture(), Mockito.any(), Mockito.any()))
                .thenThrow(RuntimeException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/getUserByName")
                        .param("userName", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.statusCode").value(400));
        String userName = userNameArgumentCaptor.getValue();
        assertEquals(userName, "test");
    }
}
