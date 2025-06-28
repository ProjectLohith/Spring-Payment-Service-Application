package org.gfg.UserService.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.gfg.UserService.enums.UserIdentificationType;
import org.gfg.UserService.enums.UserStatus;
import org.gfg.UserService.enums.UserType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(length = 30)
    String name;

    @Column(unique = true, length = 50)
    String email;

    @Column(unique = true, nullable = false, length = 40)
    String phoneNo;

    String password;

    String authorities; //ADMIN,USER

    @Enumerated(value = EnumType.STRING)
    UserType userType; //ADMIN, USER

    @Enumerated(value = EnumType.STRING)
    UserStatus userStatus;

    @Enumerated(value = EnumType.STRING)
    UserIdentificationType userIdentificationType;

    String userIdentificationValue;

    @CreationTimestamp
    Date createdOn;

    @UpdateTimestamp
    Date updatedOn;

    @Override
    public String getUsername() {
        return phoneNo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(","))
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
    }
}