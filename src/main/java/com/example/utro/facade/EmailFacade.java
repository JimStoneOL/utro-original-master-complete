package com.example.utro.facade;

import com.example.utro.dto.EmailDTO;
import com.example.utro.entity.Email;
import org.springframework.stereotype.Component;

@Component
public class EmailFacade {

    public Email emailDTOToEmail(EmailDTO emailDTO){
        Email email=new Email();
        email.setId(emailDTO.getId());
        email.setHeading(emailDTO.getHeading());
        email.setStatus(emailDTO.getStatus());
        email.setFromUserEmail(emailDTO.getFromUserEmail());
        email.setWhomUserEmail(emailDTO.getWhomUserEmail());
        email.setMessage(emailDTO.getMessage());
        return email;
    }
}
