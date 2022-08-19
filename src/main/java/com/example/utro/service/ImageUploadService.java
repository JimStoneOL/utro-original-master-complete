package com.example.utro.service;

import com.example.utro.entity.*;
import com.example.utro.exceptions.ClothNotFoundException;
import com.example.utro.exceptions.FurnitureNotFoundException;
import com.example.utro.exceptions.ImageNotFoundException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ImageUploadService {
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClothRepository clothRepository;
    private final FurnitureRepository furnitureRepository;
    private final PrincipalService principalService;

    @Autowired
    public ImageUploadService(ImageRepository imageRepository, UserRepository userRepository, ProductRepository productRepository, ClothRepository clothRepository, FurnitureRepository furnitureRepository, PrincipalService principalService) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.clothRepository = clothRepository;
        this.furnitureRepository = furnitureRepository;
        this.principalService = principalService;
    }


    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user=principalService.getUserByPrincipal(principal);
        ImageModel userProfileImage = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(userProfileImage)) {
            imageRepository.delete(userProfileImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToProduct(MultipartFile file, Principal principal, UUID productId) throws IOException {
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        ImageModel productImage=imageRepository.findByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(productImage)) {
            imageRepository.delete(productImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setProductId(product.getArticle());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToTemplateProduct(MultipartFile file, UUID productId) throws IOException {
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            throw new ProductNotFoundException("Продукт не является шаблонным");
        }
        ImageModel productImage=imageRepository.findByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(productImage)) {
            imageRepository.delete(productImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setProductId(product.getArticle());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToCloth(MultipartFile file,UUID clothId) throws IOException {
        Cloth cloth=clothRepository.findById(clothId).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        ImageModel clothImage=imageRepository.findByClothId(clothId).orElse(null);
        if (!ObjectUtils.isEmpty(clothImage)) {
            imageRepository.delete(clothImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setClothId(cloth.getArticle());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToFurniture(MultipartFile file,UUID furnitureId) throws IOException {
        Furniture furniture=furnitureRepository.findById(furnitureId).orElseThrow(()->new FurnitureNotFoundException("фурнитура не найдена"));
        ImageModel furnitureImage=imageRepository.findByClothId(furnitureId).orElse(null);
        if (!ObjectUtils.isEmpty(furnitureImage)) {
            imageRepository.delete(furnitureImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setFurnitureId(furnitureId);
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel uploadImageToOther(MultipartFile file,String other) throws IOException{
        ImageModel otherImage=imageRepository.findByOther(other).orElse(null);
        if (!ObjectUtils.isEmpty(otherImage)) {
            imageRepository.delete(otherImage);
        }
        ImageModel imageModel=new ImageModel();
        imageModel.setOther(other);
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());
        return imageRepository.save(imageModel);
    }
    public ImageModel getImageToUser(Principal principal) {
        User user=principalService.getUserByPrincipal(principal);
        ImageModel imageModel=imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }else{
            imageModel=getImageToOther("profile");
        }
        return imageModel;
    }
    public ImageModel getImageToProduct(UUID productId){
        ImageModel imageModel=imageRepository.findByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }else{
            imageModel=getImageToOther("product");
        }
        return imageModel;
    }
    public ImageModel getImageToCloth(UUID clothId){
        ImageModel imageModel=imageRepository.findByClothId(clothId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }else{
            imageModel=getImageToOther("cloth");
        }
        return imageModel;
    }
    public ImageModel getImageToFurniture(UUID furnitureId){
        ImageModel imageModel=imageRepository.findByFurnitureId(furnitureId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }else{
            imageModel=getImageToOther("furniture");
        }
        return imageModel;
    }
    public ImageModel getImageToUser(Long userId){
        ImageModel imageModel=imageRepository.findByUserId(userId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }else{
            imageModel=getImageToOther("profile");
        }
        return imageModel;
    }
    public ImageModel getImageToOther(String other){
        ImageModel imageModel=imageRepository.findByOther(other).orElseThrow(()->new ImageNotFoundException("изображение не найдено"));
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }
    public void templateProductImage(UUID oldProductId,UUID newProductId){
        ImageModel imageModel=imageRepository.findByProductId(oldProductId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            ImageModel imageModelProduct=new ImageModel();
            imageModelProduct.setProductId(newProductId);
            imageModelProduct.setImageBytes(imageModel.getImageBytes());
            imageModelProduct.setName(imageModel.getName());
            imageRepository.save(imageModelProduct);
        }
    }
    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {

        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {

        }
        return outputStream.toByteArray();
    }

}