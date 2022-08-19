package com.example.utro.web;

import com.example.utro.dto.FurnitureProductDTO;
import com.example.utro.dto.OrderedProductDTO;
import com.example.utro.entity.FurnitureProduct;
import com.example.utro.entity.OrderedProduct;
import com.example.utro.facade.OrderedProductFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.OrderedProductResponseDelete;
import com.example.utro.payload.response.OrderedProductResponseUpdate;
import com.example.utro.service.OrderedProductService;
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
@RequestMapping("api/order/product")
@CrossOrigin
public class OrderedProductController {
    @Autowired
    private OrderedProductService orderedProductService;
    @Autowired
    private OrderedProductFacade orderedProductFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> createOrderedProduct(@Valid @RequestBody OrderedProductDTO orderedProductDTO, BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        OrderedProduct orderedProduct=orderedProductService.createOrderedProduct(orderedProductDTO,principal);
        OrderedProductDTO orderedProductResponse=orderedProductFacade.orderedProductToOrderedProductDTO(orderedProduct);
        return new ResponseEntity<>(orderedProductResponse, HttpStatus.CREATED);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getOrderedProductById(@PathVariable("id") Long id,Principal principal){
        OrderedProduct orderedProduct=orderedProductService.getOrderedProductById(id,principal);
        OrderedProductDTO orderedProductDTO=orderedProductFacade.orderedProductToOrderedProductDTO(orderedProduct);
        return new ResponseEntity<>(orderedProductDTO,HttpStatus.OK);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAllOrderedProducts(Principal principal){
        List<OrderedProduct> orderedProducts=orderedProductService.getAllOrderedProducts(principal);
        List<OrderedProductDTO> orderedProductDTOList=orderedProductFacade.orderedProductListToOrderedProductDTOList(orderedProducts);
        return new ResponseEntity<>(orderedProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/any/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAnyOrderedProductById(@PathVariable("id") Long id){
        OrderedProduct orderedProduct=orderedProductService.getAnyOrderedProductById(id);
        OrderedProductDTO orderedProductDTO=orderedProductFacade.orderedProductToOrderedProductDTO(orderedProduct);
        return new ResponseEntity<>(orderedProductDTO,HttpStatus.OK);
    }
    @GetMapping("/get/any/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAnyAllOrderedProducts(){
        List<OrderedProduct> orderedProducts=orderedProductService.getAnyAllOrderedProducts();
        List<OrderedProductDTO> orderedProductDTOList=orderedProductFacade.orderedProductListToOrderedProductDTOList(orderedProducts);
        return new ResponseEntity<>(orderedProductDTOList,HttpStatus.OK);
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateOrderedProduct(@Valid @RequestBody OrderedProductDTO orderedProductDTO,BindingResult bindingResult,Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        OrderedProductResponseUpdate response=orderedProductService.updateOrderedProduct(orderedProductDTO,principal);
        if(response.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
        }else{
            return new ResponseEntity<>(response.getBody(),response.getHttpStatus());
        }
    }
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteOrderedProduct(@PathVariable("id") Long id,Principal principal){
        OrderedProductResponseDelete response=orderedProductService.deleteOrderedProduct(id,principal);
        return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
    }

    @GetMapping("/get/order/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<Object> getOrderedProductByOrderId(@PathVariable("id") UUID id, Principal principal){
        List<OrderedProduct> orderedProductList;
        try{
            orderedProductList=orderedProductService.getOrderedProductListByOrderId(id,principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<OrderedProductDTO> orderedProductDTOList=orderedProductFacade.orderedProductListToOrderedProductDTOList(orderedProductList);
        return new ResponseEntity<>(orderedProductDTOList,HttpStatus.OK);
    }
    @GetMapping("/get/any/order/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyOrderedProductByOrderId(@PathVariable("id") UUID id){
        List<OrderedProduct> orderedProductList;
        try{
            orderedProductList=orderedProductService.getAnyOrderedProductListByOrderId(id);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<OrderedProductDTO> orderedProductDTOList=orderedProductFacade.orderedProductListToOrderedProductDTOList(orderedProductList);
        return new ResponseEntity<>(orderedProductDTOList,HttpStatus.OK);
    }
}
