package com.example.utro.web;


import com.example.utro.dto.UserDTO;
import com.example.utro.entity.User;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.UserRoleResponse;
import com.example.utro.service.UserService;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @PostMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        User user=userService.updateUser(userDTO,principal);
        UserDTO updatedUser=new UserDTO();
        BeanUtils.copyProperties(user,updatedUser);
        ResponseEntity<Object> responseEntity=new ResponseEntity<>(new MessageResponse("user updated"), HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping("/get/profile")
    public ResponseEntity<Object> getProfile(Principal principal){
        UserRoleResponse userRoleResponse=userService.getProfile(principal);
        return new ResponseEntity<>(userRoleResponse,HttpStatus.OK);
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAllUsers(Principal principal){
        List<UserRoleResponse> userRoleResponse=userService.getAllUsers(principal);
        return new ResponseEntity<>(userRoleResponse,HttpStatus.OK);
    }

}