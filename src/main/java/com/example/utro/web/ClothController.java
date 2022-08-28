package com.example.utro.web;

import com.example.utro.dto.ClothDTO;
import com.example.utro.entity.Cloth;
import com.example.utro.facade.ClothFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ClothService;
import com.example.utro.validations.ResponseErrorValidation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;

@RestController
@RequestMapping("api/cloth")
@CrossOrigin
public class ClothController {
    @Autowired
    private ClothService clothService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private ClothFacade clothFacade;

    @PostMapping("/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createCloth(@Valid @RequestBody ClothDTO clothDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Cloth cloth=clothService.createCloth(clothDTO);
        ClothDTO response=clothFacade.clothToClothDTO(cloth);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllCloth(){
        List<Cloth> clothList=clothService.getAllCloth();
        List<ClothDTO> clothDTOList=clothFacade.clothListToClothDTOList(clothList);
        return new ResponseEntity<>(clothDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/{article}")
    public ResponseEntity<Object> getClothById(@PathVariable("article") UUID article){
        Cloth cloth;
        try {
            cloth = clothService.getClothById(article);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothDTO clothDTO=clothFacade.clothToClothDTO(cloth);
        return new ResponseEntity<>(clothDTO,HttpStatus.OK);
    }

    @PostMapping("/delete/{clothId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteClothById(@PathVariable("clothId") UUID clothId){
        MessageResponse messageResponse=clothService.deleteCloth(clothId);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}
