package com.stibodx.demo.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserViewRequest {

    @JsonProperty("name")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @JsonProperty("password")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @JsonProperty("email")
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^(?=\\S+@\\S+\\.\\S+$).*$", message = "Invalid email format")
    private String email;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phoneNumber")
    @Pattern(regexp = "^(|\\+(?:[0-9] ?){6,14}[0-9])$", message = "Invalid phone number format")
    private String phoneNumber;

    @JsonProperty("dni")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "Invalid DNI format")
    private String dni;

    @JsonProperty("street")
    private String street;

    @JsonProperty("auths")
    private List<String> authorities;
}
