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
        ClothProduct clothProduct=clothProductService.createClothProduct(clothProductDTO, principal);
        ClothProductDTO response=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAllClothProduct(Principal principal){
        List<ClothProduct> clothProductList=clothProductService.getAllClothProduct(principal);
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getClothProductById(@PathVariable("id") Long id, Principal principal){
        ClothProduct clothProduct = clothProductService.getClothProductById(id, principal);
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO,BindingResult bindingResult,Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProductResponseUpdate clothProductResponseUpdate = clothProductService.updateClothProduct(clothProductDTO, principal);
        if(clothProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(clothProductResponseUpdate.getMessage(),clothProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(clothProductResponseUpdate.getBody(),clothProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteClothProduct(@PathVariable("id") Long id,Principal principal){
        ClothProductResponseDelete clothProductResponseDelete = clothProductService.deleteClothProduct(id, principal);
        return new ResponseEntity<>(clothProductResponseDelete.getMessage(),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createTemplateClothProduct(@Valid @RequestBody ClothProductDTO clothProductDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ClothProduct clothProduct = clothProductService.createTemplateClothProduct(clothProductDTO);
        ClothProductDTO responseClothProduct=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(responseClothProduct,HttpStatus.CREATED);
    }
    @GetMapping("/template/get/{id}")
    public ResponseEntity<Object> getTemplateClothProductById(@PathVariable("id") Long id){
        ClothProduct clothProduct=clothProductService.getTemplateClothProductById(id);
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
    @GetMapping("/template/get/product/{id}")
    public ResponseEntity<Object> getTemplateClothProductByProductId(@PathVariable("id") UUID id){
        List<ClothProduct> clothProductList=clothProductService.getTemplateClothProductListByProductId(id);
        List<ClothProductDTO> clothProductDTOList=clothProductFacade.clothProductListToClothProductDTOList(clothProductList);
        return new ResponseEntity<>(clothProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/product/{id}")
    public ResponseEntity<Object> getClothProductByProductId(@PathVariable("id") UUID id,Principal principal){
        List<ClothProduct> clothProductList=clothProductService.getClothProductListByProductId(id,principal);
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
        ClothProductResponseUpdate clothProductResponseUpdate=clothProductService.updateTemplateClothProduct(clothProductDTO);
        if(clothProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(clothProductResponseUpdate.getMessage(),clothProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(clothProductResponseUpdate.getBody(),clothProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/template/delete/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteTemplateClothProduct(@PathVariable("id") Long id){
        ClothProductResponseDelete clothProductResponseDelete=clothProductService.deleteTemplateClothProduct(id);
        return new ResponseEntity<>(clothProductResponseDelete.getMessage(),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/delete/product/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteAllClothProductByTemplateProductId(@PathVariable("productId") UUID productId){
        ClothProductResponseDelete clothProductResponseDelete=clothProductService.deleteAllClothProductByTemplateProductId(productId);
        return new ResponseEntity<>(new MessageResponse(clothProductResponseDelete.getMessage()),clothProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/delete/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteAllClothProductByProductId(@PathVariable("productId") UUID productId,Principal principal){
        ClothProductResponseDelete clothProductResponseDelete=clothProductService.deleteAllClothProductByProductId(productId,principal);
        return new ResponseEntity<>(new MessageResponse(clothProductResponseDelete.getMessage()),clothProductResponseDelete.getHttpStatus());
    }
    @GetMapping("/get/any/product/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyClothProductByProductId(@PathVariable("id") UUID id){
        List<ClothProduct> clothProductList=clothProductService.getAnyClothProductListByProductId(id);
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
        ClothProduct clothProduct=clothProductService.getAnyClothProductById(id);
        ClothProductDTO clothProductDTO=clothProductFacade.clothProductToClothProductDTO(clothProduct);
        return new ResponseEntity<>(clothProductDTO,HttpStatus.OK);
    }
}
