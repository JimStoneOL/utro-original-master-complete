package com.example.utro.web;

import com.example.utro.dto.FurnitureProductDTO;
import com.example.utro.entity.FurnitureProduct;
import com.example.utro.facade.FurnitureProductFacade;
import com.example.utro.payload.response.FurnitureProductResponseDelete;
import com.example.utro.payload.response.FurnitureProductResponseUpdate;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.FurnitureProductService;
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
import java.util.UUID;


@RestController
@RequestMapping("api/furniture/product")
@CrossOrigin
public class FurnitureProductController {
    @Autowired
    private ResponseErrorValidation responseErrorValidation;
    @Autowired
    private FurnitureProductService furnitureProductService;

    @Autowired
    private FurnitureProductFacade furnitureProductFacade;


    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> createFurnitureProduct(@Valid @RequestBody FurnitureProductDTO furnitureProductDTO,BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureProduct furnitureProduct=furnitureProductService.createFurnitureProduct(furnitureProductDTO,principal);
        FurnitureProductDTO response=furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProduct);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAllFurnitureProduct(Principal principal){
        List<FurnitureProduct> furnitureProductList = furnitureProductService.getAllFurnitureProduct(principal);
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProductList);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getFurnitureProductById(@PathVariable("id") Long id, Principal principal){
        FurnitureProduct furnitureProduct = furnitureProductService.getFurnitureProductById(id, principal);
        FurnitureProductDTO furnitureProductDTO=furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProduct);
        return new ResponseEntity<>(furnitureProductDTO,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateFurnitureProduct(@Valid @RequestBody FurnitureProductDTO furnitureProductDTO,BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureProductResponseUpdate furnitureProductResponseUpdate = furnitureProductService.updateFurnitureProduct(furnitureProductDTO, principal);
        if(furnitureProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(furnitureProductResponseUpdate.getMessage(),furnitureProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(furnitureProductResponseUpdate.getBody(),furnitureProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteFurnitureProduct(@PathVariable("id") Long id,Principal principal){
        FurnitureProductResponseDelete furnitureProductResponseDelete=furnitureProductService.deleteFurnitureProduct(id, principal);
        return new ResponseEntity<>(furnitureProductResponseDelete.getMessage(),furnitureProductResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createTemplateFurnitureProduct(@Valid @RequestBody FurnitureProductDTO furnitureProductDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureProduct furnitureProduct=furnitureProductService.createTemplateFurnitureProduct(furnitureProductDTO);
        FurnitureProductDTO responseFurnitureProduct=furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProduct);
        return new ResponseEntity<>(responseFurnitureProduct,HttpStatus.CREATED);
    }
    @GetMapping("/template/get/{id}")
    public ResponseEntity<Object> getTemplateFurnitureProductById(@PathVariable("id") Long id){
        FurnitureProduct furnitureProduct=furnitureProductService.getTemplateFurnitureProductById(id);
        FurnitureProductDTO furnitureProductDTO=furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProduct);
        return new ResponseEntity<>(furnitureProductDTO,HttpStatus.OK);
    }
    @GetMapping("/template/get/all")
    public ResponseEntity<Object> getAllTemplateFurnitureProduct(){
        List<FurnitureProduct> furnitureProducts=furnitureProductService.getAllTemplateFurnitureProduct();
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProducts);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/template/get/product/{id}")
    public ResponseEntity<Object> getTemplateFurnitureProductByProductId(@PathVariable("id") UUID id){
        List<FurnitureProduct> furnitureProductList=furnitureProductService.getTemplateFurnitureProductListByProductId(id);
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProductList);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/product/{id}")
    public ResponseEntity<Object> getFurnitureProductByProductId(@PathVariable("id") UUID id,Principal principal){
        List<FurnitureProduct> furnitureProductList=furnitureProductService.getFurnitureProductListByProductId(id,principal);
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProductList);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @PostMapping("/template/update")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> updateTemplateFurnitureProduct(@Valid @RequestBody FurnitureProductDTO furnitureProductDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        FurnitureProductResponseUpdate furnitureProductResponseUpdate=furnitureProductService.updateTemplateFurnitureProduct(furnitureProductDTO);
        if(furnitureProductResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(furnitureProductResponseUpdate.getMessage(),furnitureProductResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(furnitureProductResponseUpdate.getBody(),furnitureProductResponseUpdate.getHttpStatus());
        }
    }
    @PostMapping("/template/delete/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteTemplateFurnitureProduct(@PathVariable("id") Long id){
        FurnitureProductResponseDelete furnitureProductResponseDelete=furnitureProductService.deleteTemplateFurnitureProduct(id);
        return new ResponseEntity<>(furnitureProductResponseDelete.getMessage(),furnitureProductResponseDelete.getHttpStatus());
    }
    @GetMapping("/get/any/product/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyFurnitureProductByProductId(@PathVariable("id") UUID id){
        List<FurnitureProduct> furnitureProductList=furnitureProductService.getAnyFurnitureProductListByProductId(id);
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProductList);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @PostMapping("/template/delete/product/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteAllFurnitureProductByTemplateProductId(@PathVariable("productId") UUID productId){
        FurnitureProductResponseDelete furnitureProductResponseDelete=furnitureProductService.deleteAllFurnitureProductByTemplateProductId(productId);
        return new ResponseEntity<>(new MessageResponse(furnitureProductResponseDelete.getMessage()),furnitureProductResponseDelete.getHttpStatus());
    }

    @PostMapping("/delete/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteAllFurnitureProductByProductId(@PathVariable("productId") UUID productId,Principal principal){
        FurnitureProductResponseDelete furnitureProductResponseDelete=furnitureProductService.deleteAllFurnitureProductByProductId(productId,principal);
        return new ResponseEntity<>(new MessageResponse(furnitureProductResponseDelete.getMessage()),furnitureProductResponseDelete.getHttpStatus());
    }

    @GetMapping("/get/any/all")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyAllFurnitureProduct(){
        List<FurnitureProduct> furnitureProducts=furnitureProductService.getAnyAllFurnitureProduct();
        List<FurnitureProductDTO> furnitureProductDTOList=furnitureProductFacade.furnitureProductListToFurnitureProductDTOList(furnitureProducts);
        return new ResponseEntity<>(furnitureProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/any/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyFurnitureProductById(@PathVariable("id") Long id){
        FurnitureProduct furnitureProduct=furnitureProductService.getAnyFurnitureProductById(id);
        FurnitureProductDTO furnitureProductDTO=furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProduct);
        return new ResponseEntity<>(furnitureProductDTO,HttpStatus.OK);
    }
}