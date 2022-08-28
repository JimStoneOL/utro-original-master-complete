package com.example.utro.service;


import com.example.utro.dto.ClothDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.ClothNotFoundException;
import com.example.utro.facade.ClothFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClothService {
    private final ClothRepository clothRepository;
    private final ClothFacade clothFacade;
    private final ClothProductRepository clothProductRepository;
    private final ImageRepository imageRepository;
    private final ClothBucketRepository clothBucketRepository;
    private final ClothWarehouseRepository clothWarehouseRepository;

    @Autowired
    public ClothService(ClothRepository clothRepository, ClothFacade clothFacade, ClothProductRepository clothProductRepository, ImageRepository imageRepository, ClothBucketRepository clothBucketRepository, ClothWarehouseRepository clothWarehouseRepository) {
        this.clothRepository = clothRepository;
        this.clothFacade = clothFacade;
        this.clothProductRepository = clothProductRepository;
        this.imageRepository = imageRepository;
        this.clothBucketRepository = clothBucketRepository;
        this.clothWarehouseRepository = clothWarehouseRepository;
    }
    public Cloth createCloth(ClothDTO clothDTO){
        clothDTO.setArticle(UUID.randomUUID());
        Cloth cloth=clothFacade.clothDTOtoCloth(clothDTO);
        Cloth savedCloth=clothRepository.save(cloth);
        return savedCloth;
    }
    public Cloth getClothById(UUID article){
        Cloth cloth=clothRepository.findById(article).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        return cloth;
    }
    public List<Cloth> getAllCloth(){
        List<Cloth> clothList=clothRepository.findAll();
        return clothList;
    }

    public MessageResponse deleteCloth(UUID clothId){
        Cloth cloth=clothRepository.findById(clothId).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        List<ClothProduct> clothProductList=clothProductRepository.findAllByCloth(cloth).orElse(null);
        if(clothProductList!=null){
            for(int i=0;i<clothProductList.size();i++){
                clothProductRepository.deleteById(clothProductList.get(i).getId());
            }
        }
        ImageModel imageModel=imageRepository.findByClothId(clothId).orElse(null);
        if(imageModel!=null){
            imageRepository.deleteById(imageModel.getId());
        }
        ClothBucket clothBucket=clothBucketRepository.findByClothId(clothId).orElse(null);
        if(clothBucket!=null){
            clothBucketRepository.deleteById(clothBucket.getId());
        }
        List<ClothWarehouse> clothWarehouseList=clothWarehouseRepository.findAllByCloth(cloth).orElse(null);
        if(clothWarehouseList!=null){
            for(int i=0;i<clothWarehouseList.size();i++){
                clothWarehouseRepository.deleteById(clothWarehouseList.get(i).getId());
            }
        }
        clothRepository.deleteById(clothId);
        return new MessageResponse("Ткань успешно удалена");
    }

}