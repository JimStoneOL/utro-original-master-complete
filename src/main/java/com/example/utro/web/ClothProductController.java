package com.example.utro.web;

import com.example.utro.dto.ClothProductDTO;
import com.example.utro.entity.ClothProduct;
import com.example.utro.facade.ClothProductFacade;
import com.example.utro.payload.response.ClothProductResponseDelete;
import com.example.utro.payload.response.ClothProductResponseUpdate;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ClothProductService;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cloth/product")
@CrossOrigin

public class ClothProductController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private ClothProductFacade clothProductFacade;
    @Autowired
    private ClothProductService clothProductService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> createClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProduct clothProduct;
        try {
            clothProduct = clothProductService.createClothProduct(clothProductDTO, principal);
        }catch(Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothProductDTO response=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAllClothProduct(Principal principal){
        List<ClothProduct> clothProductList;
        try {
            clothProductList = clothProductService.getAllClothProduct(principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getClothProductById(@PathVariable("id") Long id, Principal principal){
        ClothProduct clothProduct;
        try {
            clothProduct = clothProductService.getClothProductById(id, principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO,BindingResult bindingResult,Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProductResponseUpdate clothProductResponseUpdate;
        try {
            clothProductResponseUpdate = clothProductService.updateClothProduct(clothProductDTO, principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(clothProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(clothProductResponseUpdate.getMessage(),clothProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(clothProductResponseUpdate.getBody(),clothProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteClothProduct(@PathVariable("id") Long id,Principal principal){
        ClothProductResponseDelete clothProductResponseDelete;
        try {
            clothProductResponseDelete = clothProductService.deleteClothProduct(id, principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(clothProductResponseDelete.getMessage(),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createTemplateClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProduct clothProduct;
        try {
            clothProduct = clothProductService.createTemplateClothProduct(clothProductDTO);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothProductDTO responseClothProduct=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(responseClothProduct,HttpStatus.CREATED);
    }
    @GetMapping("/template/get/{id}")
    public ResponseEntity<Object> getTemplateClothProductById(@PathVariable("id") Long id){
        ClothProduct clothProduct;
        try{
            clothProduct=clothProductService.getTemplateClothProductById(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
    @GetMapping("/template/get/product/{id}")
    public ResponseEntity<Object> getTemplateClothProductByProductId(@PathVariable("id") UUID id){
        List<ClothProduct> clothProductList;
        try{
            clothProductList=clothProductService.getTemplateClothProductListByProductId(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/product/{id}")
    public ResponseEntity<Object> getClothProductByProductId(@PathVariable("id") UUID id,Principal principal){
        List<ClothProduct> clothProductList;
        try{
            clothProductList=clothProductService.getClothProductListByProductId(id,principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/template/get/all")
    public ResponseEntity<Object> getAllTemplateClothProduct(){
        List<ClothProduct> clothProducts=clothProductService.getAllTemplateClothProduct();
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProducts);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @PostMapping("/template/update")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> updateTemplateClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProductResponseUpdate clothProductResponseUpdate;
        try{
            clothProductResponseUpdate=clothProductService.updateTemplateClothProduct(clothProductDTO);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(clothProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(clothProductResponseUpdate.getMessage(),clothProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(clothProductResponseUpdate.getBody(),clothProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/template/delete/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteTemplateClothProduct(@PathVariable("id") Long id){
        ClothProductResponseDelete clothProductResponseDelete;
        try{
            clothProductResponseDelete=clothProductService.deleteTemplateClothProduct(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(clothProductResponseDelete.getMessage(),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/delete/product/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteAllClothProductByTemplateProductId(@PathVariable("productId") UUID productId){
        ClothProductResponseDelete clothProductResponseDelete;
        try{
            clothProductResponseDelete=clothProductService.deleteAllClothProductByTemplateProductId(productId);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new MessageResponse(clothProductResponseDelete.getMessage()),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/delete/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteAllClothProductByProductId(@PathVariable("productId") UUID productId,Principal principal){
        ClothProductResponseDelete clothProductResponseDelete;
        try{
            clothProductResponseDelete=clothProductService.deleteAllClothProductByProductId(productId,principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new MessageResponse(clothProductResponseDelete.getMessage()),clothProductResponseDelete.getHttpStatus());
    }
    @GetMapping("/get/any/product/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyClothProductByProductId(@PathVariable("id") UUID id){
        List<ClothProduct> clothProductList;
        try{
            clothProductList=clothProductService.getAnyClothProductListByProductId(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/any/all")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyAllClothProduct(){
        List<ClothProduct> clothProducts=clothProductService.getAnyAllClothProduct();
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProducts);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/any/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyClothProductById(@PathVariable("id") Long id){
        ClothProduct clothProduct;
        try{
            clothProduct=clothProductService.getAnyClothProductById(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
}
