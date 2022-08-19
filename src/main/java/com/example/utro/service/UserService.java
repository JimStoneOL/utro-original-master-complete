package com.example.utro.service;

import com.example.utro.dto.UserDTO;
import com.example.utro.entity.User;
import com.example.utro.facade.UserFacade;
import com.example.utro.payload.response.UserRoleResponse;
import com.example.utro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PrincipalService principalService;
    private final UserFacade userFacade;

    public UserService(UserRepository userRepository, PrincipalService principalService, UserFacade userFacade) {
        this.userRepository = userRepository;
        this.principalService = principalService;
        this.userFacade = userFacade;
    }

    public User updateUser(UserDTO userDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        userRepository.save(user);
        return user;
    }
    public UserRoleResponse getProfile(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        UserRoleResponse response=userFacade.userToUserRoleResponse(user);
        return response;
    }
    public List<UserRoleResponse> getAllUsers(Principal principal){
        List<User> users=userRepository.findAll();
        User user=principalService.getUserByPrincipal(principal);
        List<UserRoleResponse> userRoleResponseList=userFacade.userListToUserRoleResponseList(users,user);
        return userRoleResponseList;
    }
}
