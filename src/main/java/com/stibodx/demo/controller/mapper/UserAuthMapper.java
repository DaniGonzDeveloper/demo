package com.stibodx.demo.controller.mapper;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.utils.UserAuth;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
@Mapper(componentModel = "spring")
public interface UserAuthMapper {
    @Mapping(source = "name", target = "username")
    @Mapping(source = "authorities", target = "authorities", qualifiedByName = "authToAuthString")
    @Mapping(source = "authorities", target = "authoritiesIds", qualifiedByName = "authToAuthIds")
    public UserAuth userEntityToUserAuth(User userEntity);
    @Named("authToAuthString")
    default List<String> authToAuthString(List<Authorities> auths) {
        return auths.stream().map(Authorities::getRole).toList();
    }
    @Named("authToAuthIds")
    default List<Integer> authToAuthIds(List<Authorities> auths) {
        return auths.stream().map(Authorities::getId).toList();
    }
}
