package com.stibodx.demo.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserViewResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("password")
    private String password;
    @JsonProperty("email")
    private String email;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("dni")
    private String dni;
    @JsonProperty("street")
    private String street;
    @JsonProperty("signUpDate")
    private String signUpDate;
    @JsonProperty("auths")
    private List<String> authorities;

}
