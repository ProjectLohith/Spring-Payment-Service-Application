package org.gfg.UserService.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gfg.UserService.enums.UserIdentificationType;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    String name;

    String email;

    @NotBlank
    String phoneNo;

    @NotBlank
    String password;

    @NotNull
    UserIdentificationType userIdentificationType;

    @NotNull
    String userIdentificationValue;
}