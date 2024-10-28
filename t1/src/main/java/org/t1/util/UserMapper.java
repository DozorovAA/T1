package org.t1.util;


import org.springframework.stereotype.Component;
import org.t1.dto.UserDto;
import org.t1.entity.User;


public class UserMapper {

    public static User toEntity(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .middleName(dto.getMiddleName())
                .build();
    }

}
