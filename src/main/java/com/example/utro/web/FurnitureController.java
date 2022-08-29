package com.example.utro.web;

import com.example.utro.dto.FurnitureDTO;
import com.example.utro.entity.Furniture;
import com.example.utro.facade.FurnitureFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.FurnitureService;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/furniture")
@CrossOrigin
public class FurnitureController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private FurnitureService furnitureService;

    @Autowired
    private FurnitureFacade furnitureFacade;

    @PostMapping("/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createFurniture(@Valid @RequestBody FurnitureDTO furnitureDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Furniture furniture=furnitureService.createFurniture(furnitureDTO);
        FurnitureDTO response=furnitureFacade.furnitureToFurnitureDTO(furniture);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllFurniture(){
        List<Furniture> furnitureList=furnitureService.getAllFurniture();
        List<FurnitureDTO> furnitureDTOList=furnitureFacade.furnitureListToFurnitureDTOList(furnitureList);
        return new ResponseEntity<>(furnitureDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/{article}")
    public ResponseEntity<Object> getFurnitureById(@PathVariable("article") UUID article){
        Furniture furniture = furnitureService.getFurnitureById(article);
        FurnitureDTO furnitureDTO=furnitureFacade.furnitureToFurnitureDTO(furniture);
        return new ResponseEntity<>(furnitureDTO,HttpStatus.OK);
    }
    @PostMapping("/delete/{furnitureId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteFurnitureById(@PathVariable("furnitureId") UUID furnitureId){
        MessageResponse messageResponse=furnitureService.deleteFurniture(furnitureId);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}
