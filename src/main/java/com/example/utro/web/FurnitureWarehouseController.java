package com.example.utro.web;

import com.example.utro.dto.FurnitureWarehouseDTO;
import com.example.utro.entity.FurnitureWarehouse;
import com.example.utro.facade.FurnitureWarehouseFacade;
import com.example.utro.payload.response.FurnitureWarehouseResponseDelete;
import com.example.utro.payload.response.FurnitureWarehouseResponseUpdate;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.FurnitureWarehouseService;
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
@RequestMapping("api/furniture/warehouse")
@CrossOrigin
public class FurnitureWarehouseController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private FurnitureWarehouseService furnitureWarehouseService;
    @Autowired
    private FurnitureWarehouseFacade furnitureWarehouseFacade;


    @PostMapping("/create")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> createFurnitureWarehouse(@Valid @RequestBody FurnitureWarehouseDTO furnitureWarehouseDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureWarehouse furnitureWarehouse;
        try {
            furnitureWarehouse = furnitureWarehouseService.createFurnitureWarehouse(furnitureWarehouseDTO);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        FurnitureWarehouseDTO response=furnitureWarehouseFacade.furnitureWarehouseToFurnitureWarehouseDTO(furnitureWarehouse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAllFurnitureWarehouse(){
        List<FurnitureWarehouse> furnitureWarehouseList=furnitureWarehouseService.getAllFurnitureWarehouses();
        List<FurnitureWarehouseDTO> furnitureWarehouseDTOList=furnitureWarehouseFacade.furnitureWarehouseListToFurnitureWarehouseDTOList(furnitureWarehouseList);
        return new ResponseEntity<>(furnitureWarehouseDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/all/furniture/{furnitureId}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAllFurnitureWarehouseByFurnitureId(@PathVariable("furnitureId") UUID id){
        List<FurnitureWarehouse> furnitureWarehouseList=furnitureWarehouseService.getAllFurnitureWarehousesByFurnitureId(id);
        List<FurnitureWarehouseDTO> furnitureWarehouseDTOList=furnitureWarehouseFacade.furnitureWarehouseListToFurnitureWarehouseDTOList(furnitureWarehouseList);
        return new ResponseEntity<>(furnitureWarehouseDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> getFurnitureWarehouseById(@PathVariable("id") Long id){
        FurnitureWarehouse furnitureWarehouse;
        try {
            furnitureWarehouse = furnitureWarehouseService.getFurnitureWarehouseById(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        FurnitureWarehouseDTO furnitureWarehouseDTO=furnitureWarehouseFacade.furnitureWarehouseToFurnitureWarehouseDTO(furnitureWarehouse);
        return new ResponseEntity<>(furnitureWarehouseDTO,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> updateFurnitureWarehouse(@Valid @RequestBody FurnitureWarehouseDTO furnitureWarehouseDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureWarehouseResponseUpdate response;
        try {
            response = furnitureWarehouseService.updateFurnitureWarehouse(furnitureWarehouseDTO);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(response.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
        }else{
            return new ResponseEntity<>(response.getBody(),response.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('STOREKEEPER') or hasRole('DIRECTOR')")
    public ResponseEntity<Object> deleteFurnitureWarehouse(@PathVariable("id") Long id){
        FurnitureWarehouseResponseDelete response;
        try {
            response = furnitureWarehouseService.deleteFurnitureWarehouse(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new MessageResponse(response.getMessage()),response.getHttpStatus());
    }
}