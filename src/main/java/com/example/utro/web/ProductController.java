package com.example.utro.web;


import com.example.utro.dto.ProductDTO;
import com.example.utro.entity.Product;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.facade.ProductFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.ProductResponseDelete;
import com.example.utro.payload.response.ProductResponseUpdate;
import com.example.utro.service.ProductService;
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
@RequestMapping("api/product")
@CrossOrigin
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductFacade productFacade;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> createProduct(@Valid @RequestBody ProductDTO productDTO,BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Product product=productService.createProduct(productDTO,principal);
        ProductDTO productDTOResponse=productFacade.productToProductDTO(product);
        return new ResponseEntity<>(productDTOResponse, HttpStatus.CREATED);
    }
    @PostMapping("/template/add/{article}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> addProductFromTemplate(@PathVariable("article") UUID article, Principal principal){
        Product customerProduct;
        try {
            customerProduct = productService.addTemplateProduct(article, principal);
        }catch(Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ProductDTO response=productFacade.productToProductDTO(customerProduct);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @GetMapping("/get/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getProductById(@PathVariable("id") UUID article, Principal principal){
        Product product;
        try {
            product = productService.getProductById(article, principal);
        }catch(Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ProductDTO productDTO=productFacade.productToProductDTO(product);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getAllProducts(Principal principal){
        List<Product> products;
        try {
            products = productService.getAllProducts(principal);
        }
        catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        List<ProductDTO> productDTOList=productFacade.productListToProductDTOList(products);
        return new ResponseEntity<>(productDTOList,HttpStatus.OK);
    }
    @PostMapping("/delete/{article}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteProduct(@PathVariable("article") UUID article,Principal principal){
        ProductResponseDelete productResponseDelete;
        try {
            productResponseDelete = productService.deleteProduct(article, principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productResponseDelete.getMessage(),productResponseDelete.getHttpStatus());
    }
    @PostMapping("/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateProduct(@Valid @RequestBody ProductDTO productDTO,BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ProductResponseUpdate productResponseUpdate;
        try {
            productResponseUpdate = productService.updateProduct(productDTO, principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(productResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(productResponseUpdate.getMessage(),productResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(productResponseUpdate.getBody(),productResponseUpdate.getHttpStatus());
        }
    }
    @GetMapping("/template/get/{id}")
    public ResponseEntity<Object> getTemplateProductById(@PathVariable("id") UUID article){
        Product product;
        try {
            product = productService.getTemplateProductById(article);
        }
        catch (Exception e){
         return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        ProductDTO productDTO=productFacade.productToProductDTO(product);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }
    @GetMapping("/template/get/all")
    public ResponseEntity<Object> getAllTemplateProducts(){
        List<Product> products=productService.getAllTemplateProducts();
        List<ProductDTO> productDTOList=productFacade.productListToProductDTOList(products);
        return new ResponseEntity<>(productDTOList,HttpStatus.OK);
    }
    @PostMapping("/template/create")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> createTemplateProduct(@Valid @RequestBody ProductDTO productDTO,BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Product product=productService.createTemplateProduct(productDTO);
        return new ResponseEntity<>(productFacade.productToProductDTO(product),HttpStatus.CREATED);
    }
    @GetMapping("/get/any/{article}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyProductById(@PathVariable("article") UUID article){
        Product product;
        try {
            product = productService.getProductById(article);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productFacade.productToProductDTO(product), HttpStatus.OK);
    }
    @GetMapping("/get/any/all")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAnyAllProducts(){
        List<Product> products=productService.getAllProducts();
        return new ResponseEntity<>(productFacade.productListToProductDTOList(products),HttpStatus.OK);
    }
    @PostMapping("/template/delete/{article}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteTemplateProduct(@PathVariable("article") UUID article){
        ProductResponseDelete productResponseDelete;
        try {
            productResponseDelete = productService.deleteTemplateProduct(article);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(productResponseDelete.getMessage(),productResponseDelete.getHttpStatus());
    }
    @PostMapping("/template/update")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> updateTemplateProduct(@Valid @RequestBody ProductDTO productDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        ProductResponseUpdate productResponseUpdate;
        try {
            productResponseUpdate = productService.updateTemplateProduct(productDTO);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(productResponseUpdate.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(productResponseUpdate.getMessage(),productResponseUpdate.getHttpStatus());
        }else{
            return new ResponseEntity<>(productResponseUpdate.getBody(),productResponseUpdate.getHttpStatus());
        }
    }
}
//todo -------------customers----------------
//todo createProduct
//todo addTemplateProduct
//todo getProductById
//todo getAllProducts
//todo deleteProduct
//todo updateProduct
//todo getTemplateProductById
//todo getAllTemplateProducts
//todo ------------director/manager-------------
//todo createTemplateProduct
//todo getAllProducts
