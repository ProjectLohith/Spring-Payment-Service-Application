package org.gfg.UserService.mapper;

import lombok.experimental.UtilityClass;
import org.gfg.UserService.dto.CreateUserRequest;
import org.gfg.UserService.enums.UserStatus;
import org.gfg.UserService.model.User;

@UtilityClass
public class UserMapper {

    public User mapToUser(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNo(request.getPhoneNo())
                .userIdentificationType(request.getUserIdentificationType())
                .userIdentificationValue(request.getUserIdentificationValue())
                .userStatus(UserStatus.ACTIVE).build();

    }
}