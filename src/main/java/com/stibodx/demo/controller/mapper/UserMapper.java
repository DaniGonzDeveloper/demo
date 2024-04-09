package com.stibodx.demo.controller.mapper;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.views.UserViewRequest;
import com.stibodx.demo.views.UserViewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "signUpDate", target = "signUpDate", qualifiedByName = "localDateToString")
    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authToAuthString")
    @Mapping(source = "password", target = "password", qualifiedByName = "removePassword")
    public UserViewResponse userEntityToDto(User userEntity);
    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "defaultAuths")
    @Mapping(source = "name", target = "name", qualifiedByName = "toLoweCase")
    @Mapping(source = "dni", target = "dni", qualifiedByName = "toLoweCase")
    @Mapping(source = "email", target = "email", qualifiedByName = "toLoweCase")
    public User userDtoToEntity(UserViewRequest userViewRequest);
    @Named("localDateToString")
    default String localDateToString(LocalDate date) {
        return date.toString();
    }
    @Named("authToAuthString")
    default List<String> authToAuthString(List<Authorities> auths) {
        return auths.stream().map(Authorities::getRole).toList();
    }
    @Named("defaultAuths")
    default List<Authorities> defaultAuths(List<String> authsDto) {
        return null;
    }
    @Named("removePassword")
    default String defaultAuths(String password) {
        return null;
    }
    @Named("toLoweCase")
    default String toLoweCase(String value) {
        return Objects.isNull(value) ? null : value.toLowerCase();
    }

}
