package com.example.utro.web;

import com.example.utro.dto.ClothWarehouseDTO;
import com.example.utro.entity.ClothWarehouse;
import com.example.utro.facade.ClothWarehouseFacade;
import com.example.utro.payload.response.ClothWarehouseResponseDelete;
import com.example.utro.payload.response.ClothWarehouseResponseUpdate;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ClothWarehouseService;
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
@RequestMapping("api/cloth/warehouse")
@CrossOrigin
public class ClothWarehouseController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private ClothWarehouseService clothWarehouseService;
    @Autowired
    private ClothWarehouseFacade clothWarehouseFacade;

    @PostMapping("/create")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> createClothWarehouse(@Valid @RequestBody ClothWarehouseDTO clothWarehouseDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothWarehouse clothWarehouse= clothWarehouseService.createClothWarehouse(clothWarehouseDTO);
        ClothWarehouseDTO response=clothWarehouseFacade.clothWarehouseToClothWarehouseDTO(clothWarehouse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAllClothWarehouse(){
        List<ClothWarehouse> clothWarehouseList=clothWarehouseService.getAllClothWarehouses();
        List<ClothWarehouseDTO> clothWarehouseDTOList=clothWarehouseFacade.clothWarehouseListToClothWarehouseDTOList(clothWarehouseList);
        return new ResponseEntity<>(clothWarehouseDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/all/cloth/{clothId}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAllClothWarehouseByClothId(@PathVariable("clothId") UUID id){
        List<ClothWarehouse> clothWarehouseList=clothWarehouseService.getAllClothWarehousesByClothId(id);
        List<ClothWarehouseDTO> clothWarehouseDTOList=clothWarehouseFacade.clothWarehouseListToClothWarehouseDTOList(clothWarehouseList);
        return new ResponseEntity<>(clothWarehouseDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getClothWarehouseById(@PathVariable("id") Long id){
        ClothWarehouse clothWarehouse = clothWarehouseService.getClothWarehouseById(id);
        ClothWarehouseDTO clothWarehouseDTO=clothWarehouseFacade.clothWarehouseToClothWarehouseDTO(clothWarehouse);
        return new ResponseEntity<>(clothWarehouseDTO,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> updateClothWarehouse(@Valid @RequestBody ClothWarehouseDTO clothWarehouseDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothWarehouseResponseUpdate response = clothWarehouseService.updateClothWarehouse(clothWarehouseDTO);
        if(response.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
        }else{
            return new ResponseEntity<>(response.getBody(),response.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> deleteClothWarehouse(@PathVariable("id") Long id){
        ClothWarehouseResponseDelete response = clothWarehouseService.deleteClothWarehouse(id);
        return new ResponseEntity<>(new MessageResponse(response.getMessage()),response.getHttpStatus());
    }
}