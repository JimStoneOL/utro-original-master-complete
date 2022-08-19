package com.example.utro.web;

import com.example.utro.entity.ImageModel;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("api/image")
@CrossOrigin("*")
public class ImageUploadController {
    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping(path = "/upload/avatar",consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadImageToUser(@RequestParam MultipartFile file, Principal principal) throws IOException {
        imageUploadService.uploadImageToUser(file, principal);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"), HttpStatus.OK);
    }
    @PostMapping("/upload/product/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> uploadImageToProduct(@PathVariable UUID productId,@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        imageUploadService.uploadImageToProduct(file, principal, productId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }
    @PostMapping("/upload/template/product/{productId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> uploadImageToTemplateProduct(@PathVariable UUID productId,@RequestParam("file") MultipartFile file) throws IOException {
        imageUploadService.uploadImageToTemplateProduct(file,productId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }

    @PostMapping("/upload/cloth/{clothId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> uploadImageToCloth(@PathVariable UUID clothId,@RequestParam("file") MultipartFile file) throws IOException {
        imageUploadService.uploadImageToCloth(file, clothId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }
    @PostMapping("/upload/furniture/{furnitureId}")
    @PreAuthorize("hasRole('DIRECTOR') or hasRole('MANAGER')")
    public ResponseEntity<Object> uploadImageToFurniture(@PathVariable UUID furnitureId,@RequestParam("file") MultipartFile file) throws IOException {
        imageUploadService.uploadImageToFurniture(file, furnitureId);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }
    @PostMapping("/upload/other/{otherName}")
    public ResponseEntity<Object> uploadImageToOther(@PathVariable String otherName,@RequestParam("file") MultipartFile file) throws IOException {
        imageUploadService.uploadImageToOther(file,otherName);
        return new ResponseEntity<>(new MessageResponse("Изображение успешно загружено"),HttpStatus.OK);
    }
    @GetMapping("/get/avatar")
    public ResponseEntity<Object> getImageForUser(Principal principal){
        ImageModel imageModel=imageUploadService.getImageToUser(principal);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
    @GetMapping("/get/product/{productId}")
    public ResponseEntity<Object> getImageToProduct(@PathVariable("productId") UUID productId){
        ImageModel imageModel=imageUploadService.getImageToProduct(productId);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
    @GetMapping("/get/cloth/{clothId}")
    public ResponseEntity<Object> getImageToCloth(@PathVariable("clothId") UUID clothId){
        ImageModel imageModel=imageUploadService.getImageToCloth(clothId);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
    @GetMapping("/get/furniture/{furnitureId}")
    public ResponseEntity<Object> getImageToFurniture(@PathVariable("furnitureId") UUID furnitureId){
        ImageModel imageModel=imageUploadService.getImageToFurniture(furnitureId);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
    @GetMapping("/get/other/{otherName}")
    public ResponseEntity<Object> getImageToOther(@PathVariable("otherName") String otherName){
        ImageModel imageModel=imageUploadService.getImageToOther(otherName);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
    @GetMapping("/get/user/{userName}")
    public ResponseEntity<Object> getImageToUser(@PathVariable("userName") Long userName){
        ImageModel imageModel=imageUploadService.getImageToUser(userName);
        return new ResponseEntity<>(imageModel,HttpStatus.OK);
    }
}