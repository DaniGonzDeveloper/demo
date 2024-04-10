package com.stibodx.demo.controller.mapper;

import com.stibodx.demo.config.WithMockCustomUser;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.views.UserViewRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserMapperTest {

    UserMapper userMapper;
    private final String TEST_VARIABLE;
    public UserMapperTest(){
        TEST_VARIABLE = "test";
        userMapper = new UserMapperImpl();
    }
    @Test
    public void dtoToEntityTest() {
        UserViewRequest userDto = UserViewRequest.builder()
                .name(TEST_VARIABLE)
                .email(TEST_VARIABLE)
                .build();
        User user = userMapper.userDtoToEntity(userDto);
        assertEquals(user.getName(), TEST_VARIABLE);
        assertEquals(user.getEmail(), TEST_VARIABLE);
        assertTrue(Objects.isNull(user.getPassword()));
    }
}
