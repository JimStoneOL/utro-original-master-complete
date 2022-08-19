package com.example.utro.facade;

import com.example.utro.entity.User;
import com.example.utro.payload.response.UserRoleResponse;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserFacade {
    public UserRoleResponse userToUserRoleResponse(User user){
        UserRoleResponse response=new UserRoleResponse();
        response.setId(user.getId());
        response.setUsername(user.getEmail());
        response.setFirstname(user.getFirstname());
        response.setLastname(user.getLastname());
        response.setRole(user.getRoles());
        return response;
    }
    public List<UserRoleResponse> userListToUserRoleResponseList(List<User> users, User currentUser){
        List<UserRoleResponse> response=new ArrayList<>();
        for(int i=0;i<users.size();i++){
            User user=users.get(i);
            if(user.getEmail()==currentUser.getEmail()){
                continue;
            }
            UserRoleResponse userRole=new UserRoleResponse();
            userRole.setId(user.getId());
            userRole.setFirstname(user.getFirstname());
            userRole.setLastname(user.getLastname());
            userRole.setUsername(user.getEmail());
            userRole.setRole(user.getRoles());
            response.add(userRole);
        }
        return response;
    }
}
