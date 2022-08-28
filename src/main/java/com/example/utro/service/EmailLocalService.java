package com.example.utro.service;

import com.example.utro.dto.EmailDTO;
import com.example.utro.entity.Email;
import com.example.utro.entity.User;
import com.example.utro.entity.enums.EmailStatus;
import com.example.utro.exceptions.UserNotFoundException;
import com.example.utro.facade.EmailFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.EmailRepository;
import com.example.utro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class EmailLocalService {

    private final EmailRepository emailRepository;
    private final PrincipalService principalService;
    private final EmailFacade emailFacade;
    private final UserRepository userRepository;

    @Autowired
    public EmailLocalService(EmailRepository emailRepository, PrincipalService principalService, EmailFacade emailFacade, UserRepository userRepository) {
        this.emailRepository = emailRepository;
        this.principalService = principalService;
        this.emailFacade = emailFacade;
        this.userRepository = userRepository;
    }

    public MessageResponse sendEmailByBot(EmailDTO emailDTO){
        Email email=emailFacade.emailDTOToEmail(emailDTO);
        emailRepository.save(email);
        return new MessageResponse("Сообщение отправлено");
    }
    public MessageResponse sendEmailByVIP(EmailDTO emailDTO,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        emailDTO.setFromUserEmail(user.getEmail());
        Email email=emailFacade.emailDTOToEmail(emailDTO);
        if(email.getStatus().equals(EmailStatus.STATUS_PRIVATE)){
            userRepository.findUserByEmail(email.getWhomUserEmail()).orElseThrow(()->new UserNotFoundException("Пользователь не найден"));
        }
        emailRepository.save(email);
        return new MessageResponse("Сообщение отправлено");
    }
    public MessageResponse sendEmailByUser(EmailDTO emailDTO,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        emailDTO.setFromUserEmail(user.getEmail());
        Email email=emailFacade.emailDTOToEmail(emailDTO);
        email.setStatus(EmailStatus.STATUS_PRIVATE);
        userRepository.findUserByEmail(email.getWhomUserEmail()).orElseThrow(()->new UserNotFoundException("Пользователь не найден"));
        emailRepository.save(email);
        return new MessageResponse("Сообщение отправлено");
    }
    public List<Email> getAllEmail(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<Email> emails=emailRepository.findAllByWhomUserEmailOrStatusOrderByCreatedDateDesc(user.getEmail(), EmailStatus.STATUS_ALL).orElse(null);
        return emails;
    }

    public MessageResponse deleteEmailById(Long id,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Email email=emailRepository.findByIdAndWhomUserEmail(id, user.getEmail()).orElse(null);
        if(email!=null){
            emailRepository.deleteById(id);
            return new MessageResponse("Сообщение удалено");
        }else{
            return new MessageResponse("Сообщение не найдено");
        }
    }
}
