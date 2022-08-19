package com.example.utro.web;

import com.example.utro.payload.request.GiveRoleRequest;
import com.example.utro.payload.response.GiveRoleResponse;
import com.example.utro.service.RoleService;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/role")
@CrossOrigin
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/give")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Object> giveRoleToUser(@RequestBody @Valid GiveRoleRequest request, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        GiveRoleResponse response=roleService.giveRoleToUser(request.getRole(),request.getUserId());
        if(response.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
        }else{
            return new ResponseEntity<>(response.getBody(),response.getHttpStatus());
        }
    }
}
