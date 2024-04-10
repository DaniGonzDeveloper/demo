package com.stibodx.demo.views;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "name", description = "userName", example = "test")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @JsonProperty("password")
    @Schema(name = "password", description = "user password", example = "test")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @JsonProperty("email")
    @Schema(name = "email", description = "user email", example = "test@gmail.com")
    @NotBlank(message = "Email is mandatory")
    @Pattern(regexp = "^(?=\\S+@\\S+\\.\\S+$).*$", message = "Invalid email format")
    private String email;

    @JsonProperty("lastName")
    @Schema(name = "lastName", description = "user lastName", example = "test")
    private String lastName;

    @JsonProperty("phoneNumber")
    @Schema(name = "phoneNumber", description = "user phone number", example = "+34615739102")
    @Pattern(regexp = "^(|\\+(?:[0-9] ?){6,14}[0-9])$", message = "Invalid phone number format")
    private String phoneNumber;

    @JsonProperty("dni")
    @Schema(name = "dni", description = "user identification", example = "76511197C")
    @Pattern(regexp = "^[0-9]{8}[A-Za-z]$", message = "Invalid DNI format")
    private String dni;

    @JsonProperty("street")
    @Schema(name = "street", description = "user street", example = "C/Antequera")
    private String street;

    @JsonProperty("auths")
    @Schema(name = "auths", description = "possibles auths for user 1 - normal, 2 - admin", example = "[\"1\"]")
    private List<String> authorities;
}
