package com.example.utro.web;

import com.example.utro.dto.EmailDTO;
import com.example.utro.entity.Email;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.EmailLocalService;
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
import java.util.List;

@RestController
@RequestMapping("api/local/email")
@CrossOrigin
public class EmailLocalController {

    @Autowired
    private EmailLocalService emailService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/send")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('STOREKEEPER')")
    public ResponseEntity<Object> sendEmailByUser(@Valid @RequestBody EmailDTO emailDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        MessageResponse messageResponse = emailService.sendEmailByUser(emailDTO, principal);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
    @GetMapping("/get/all")
    public ResponseEntity<Object> getEmails(Principal principal){
        List<Email> emails=emailService.getAllEmail(principal);
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteEmail(@PathVariable("id") Long id, Principal principal){
        MessageResponse messageResponse=emailService.deleteEmailById(id,principal);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
    @PostMapping("/vip/send")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> sendEmailByVIP(@Valid @RequestBody EmailDTO emailDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        MessageResponse messageResponse=emailService.sendEmailByVIP(emailDTO,principal);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }
}
