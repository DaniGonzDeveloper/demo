package com.stibodx.demo;

import com.stibodx.demo.entities.User;
import com.stibodx.demo.utils.JWTUtils;
import com.stibodx.demo.utils.UserAuth;
import com.stibodx.demo.views.ErrorResponse;
import com.stibodx.demo.views.TokenViewResponse;
import com.stibodx.demo.views.UserViewRequest;
import com.stibodx.demo.views.UserViewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    private final TestRestTemplate restTemplate;
    private final String secretKey;
    @Autowired
    public IntegrationTest(TestRestTemplate restTemplate, @Value("${myapp.security.secretkey}") String secretKey) {
        this.restTemplate = restTemplate;
        this.secretKey = secretKey;
    }

    @Test
    public void testUserWithLoginExists() {
        ResponseEntity<TokenViewResponse> response = restTemplate
                .withBasicAuth("test", "newTest")
                .getForEntity("/logIn", TokenViewResponse.class);

        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals("test", response.getBody().getUserName());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUserWithLoginNoExists() {
        ResponseEntity<ErrorResponse> response = restTemplate
                .withBasicAuth("testNoExist", "newTest")
                .getForEntity("/logIn", ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(401, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpNoValidUserDtoNoNeededData() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpNoValidUserDtoWrongEmail() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test");
        requestObject.setPassword("test");
        requestObject.setEmail("test");
        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpNoValidUserDtoWrongDni() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test");
        requestObject.setPassword("test");
        requestObject.setEmail("test@gmail.com");
        requestObject.setDni("123");

        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpNoValidUserDtoWrongPhoneNumber() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test");
        requestObject.setPassword("test");
        requestObject.setEmail("test@gmail.com");
        requestObject.setPhoneNumber("123");

        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpUserParamsAlredyInUse() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test");
        requestObject.setPassword("test");
        requestObject.setEmail("test@gmail.com");
        requestObject.setPhoneNumber("+34617846291");

        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testUserSignUpOk() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test2");
        requestObject.setPassword("test2");
        requestObject.setEmail("test2@gmail.com");
        requestObject.setPhoneNumber("+34617846291");

        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<TokenViewResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, TokenViewResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals("test2", response.getBody().getUserName());
    }

    @Test
    public void testUserSignUpNoRoleFound() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        UserViewRequest requestObject = new UserViewRequest();
        requestObject.setName("test2");
        requestObject.setPassword("test2");
        requestObject.setEmail("test2@gmail.com");
        requestObject.setPhoneNumber("+34617846291");
        requestObject.setAuthorities(Collections.singletonList("2"));

        HttpEntity<UserViewRequest> requestHttpEntity = new HttpEntity<>(requestObject, header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .postForEntity("/signIn", requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testGetUserByNameNormalRole() {
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(getTokenForNormalUser());

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", "test");

        HttpEntity<?> requestHttpEntity = new HttpEntity<>(header);

        ResponseEntity<UserViewResponse> response = restTemplate
                .exchange("/getUserByName?userName={userName}", HttpMethod.GET, requestHttpEntity,UserViewResponse.class, variables);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals("test", response.getBody().getName());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertTrue(Objects.isNull(response.getBody().getDni()));
    }

    @Test
    public void testGetUserByNameAdminRole() {
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(getTokenForAdminUser());

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", "test");

        HttpEntity<?> requestHttpEntity = new HttpEntity<>(header);

        ResponseEntity<UserViewResponse> response = restTemplate
                .exchange("/getUserByName?userName={userName}", HttpMethod.GET, requestHttpEntity,UserViewResponse.class, variables);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals("test", response.getBody().getName());
        assertEquals("test@gmail.com", response.getBody().getEmail());
        assertEquals("78167751C", response.getBody().getDni());
    }

    @Test
    public void testGetUserByNameNoNameRequestParam() {
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(getTokenForNormalUser());

        HttpEntity<?> requestHttpEntity = new HttpEntity<>(header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .exchange("/getUserByName", HttpMethod.GET, requestHttpEntity, ErrorResponse.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    public void testGetUserByNameNoAuth() {
        HttpHeaders header = new HttpHeaders();

        Map<String, String> variables = new HashMap<>();
        variables.put("userName", "test");

        HttpEntity<?> requestHttpEntity = new HttpEntity<>(header);

        ResponseEntity<ErrorResponse> response = restTemplate
                .exchange("/getUserByName?userName={userName}", HttpMethod.GET, requestHttpEntity, ErrorResponse.class, variables);

        assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        assertTrue(Objects.nonNull(response.getBody()));
        assertEquals(401, response.getBody().getStatusCode());
    }

    private String getTokenForNormalUser() {
        UserAuth userAuth = UserAuth.builder()
                .email("test@gmail.com")
                .username("test")
                .authorities(Collections.singletonList("ROLE_normal"))
                .authoritiesIds(Collections.singletonList(1))
                .build();
        return JWTUtils.generateToken(userAuth, secretKey);
    }
    private String getTokenForAdminUser() {
        UserAuth userAuth = UserAuth.builder()
                .email("test@gmail.com")
                .username("test")
                .authorities(Collections.singletonList("ROLE_admin"))
                .authoritiesIds(Collections.singletonList(2))
                .build();
        return JWTUtils.generateToken(userAuth, secretKey);
    }
}
