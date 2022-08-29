package com.example.utro.web;

import com.example.utro.entity.ImageModel;
import com.example.utro.entity.ProductImage;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/product/image/repository")
@CrossOrigin("*")
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping("/upload/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> uploadImageProduct(@PathVariable UUID productId, @RequestParam("file") MultipartFile file, Principal principal) throws IOException {
            productImageService.uploadImageProduct(file, principal, productId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"), HttpStatus.OK);
    }

    @PostMapping("/upload/template/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> uploadImageTemplateProduct(@PathVariable UUID productId,@RequestParam("file") MultipartFile file) throws IOException {
        productImageService.uploadImageTemplateProduct(file,productId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }

    @GetMapping("/get/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> getImageProduct(@PathVariable("productId") UUID productId,Principal principal){
        List<ProductImage> productImages=productImageService.getImageProduct(productId,principal);
        return new ResponseEntity<>(productImages,HttpStatus.OK);
    }

    @GetMapping("/get/template/{productId}")
    public ResponseEntity<Object> getTemplateImageProduct(@PathVariable("productId") UUID productId){
        List<ProductImage> productImages=productImageService.getTemplateImageProduct(productId);
        return new ResponseEntity<>(productImages,HttpStatus.OK);
    }

    @GetMapping("/get/all/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAllImageProduct(@PathVariable("productId") UUID productId){
        List<ProductImage> productImages=productImageService.getAllImageProduct(productId);
        return new ResponseEntity<>(productImages,HttpStatus.OK);
    }

    @PostMapping("/delete/{productId}/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteImageProduct(@PathVariable Long id, @PathVariable UUID productId,Principal principal) throws IOException {
            productImageService.deleteImageProduct(id, productId, principal);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно удалено"), HttpStatus.OK);
    }

    @PostMapping("/delete/template/{productId}/{id}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> deleteTemplateImageProduct(@PathVariable Long id, @PathVariable UUID productId) throws IOException {
        productImageService.deleteTemplateImageProduct(id, productId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно удалено"), HttpStatus.OK);
    }
}
