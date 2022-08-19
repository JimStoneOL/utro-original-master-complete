package com.example.utro.payload.response;

import com.example.utro.entity.Role;
import com.example.utro.entity.enums.ERole;
import lombok.Data;

import java.util.Set;

@Data
public class UserRoleResponse {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private Set<Role> role;
}
