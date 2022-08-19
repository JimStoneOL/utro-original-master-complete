package com.example.utro.service;

import com.example.utro.entity.Role;
import com.example.utro.entity.User;
import com.example.utro.entity.enums.ERole;
import com.example.utro.exceptions.UserNotFoundException;
import com.example.utro.facade.UserFacade;
import com.example.utro.payload.response.GiveRoleResponse;
import com.example.utro.payload.response.UserRoleResponse;
import com.example.utro.repository.RoleRepository;
import com.example.utro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserFacade userFacade;

    @Autowired
    public RoleService(UserRepository userRepository, RoleRepository roleRepository, UserFacade userFacade) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userFacade = userFacade;
    }

    public GiveRoleResponse giveRoleToUser(ERole role, Long userId){
        User user=userRepository.findById(userId).orElseThrow(()->new UserNotFoundException("пользователь по такому id не найден"));
        Role userRole = roleRepository
                .findByName(role)
                .orElseThrow(() -> new RuntimeException("Ошибка, роль не найдена"));
        if(user.getRoles().contains(ERole.ROLE_DIRECTOR)){
            GiveRoleResponse response=new GiveRoleResponse(null,"Ошибка, данный пользователь является директором", HttpStatus.BAD_REQUEST);
            return response;
        }else if(user.getRoles().contains(role)){
            GiveRoleResponse response=new GiveRoleResponse(null,"Ошибка, данный пользователь уже имеет такую роль", HttpStatus.BAD_REQUEST);
            return response;
        }else {
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
            User savedUser=userRepository.save(user);
            UserRoleResponse response = userFacade.userToUserRoleResponse(savedUser);
            GiveRoleResponse superLastResponse=new GiveRoleResponse(response,"Роль успешно изменена", HttpStatus.OK);
            return superLastResponse;
        }
    }
}
