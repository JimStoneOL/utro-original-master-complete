package com.example.utro.service;

import com.example.utro.dto.ClothWarehouseDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.ClothNotFoundException;
import com.example.utro.exceptions.ClothWarehouseNotFoundException;
import com.example.utro.facade.ClothWarehouseFacade;
import com.example.utro.payload.response.ClothWarehouseResponseDelete;
import com.example.utro.payload.response.ClothWarehouseResponseUpdate;
import com.example.utro.repository.ClothRepository;
import com.example.utro.repository.ClothWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class ClothWarehouseService {
    private final ClothWarehouseRepository clothWarehouseRepository;
    private final ClothWarehouseFacade clothWarehouseFacade;
    private final ClothRepository clothRepository;

    @Autowired
    public ClothWarehouseService(ClothWarehouseRepository clothWarehouseRepository, ClothWarehouseFacade clothWarehouseFacade, ClothRepository clothRepository) {
        this.clothWarehouseRepository = clothWarehouseRepository;
        this.clothWarehouseFacade = clothWarehouseFacade;
        this.clothRepository = clothRepository;
    }
    public ClothWarehouse createClothWarehouse(ClothWarehouseDTO clothWarehouseDTO){
        clothWarehouseDTO.setId(generateRandomNumberId());
        ClothWarehouse clothWarehouse=clothWarehouseFacade.clothWarehouseDTOToClothWarehouse(clothWarehouseDTO);
        ClothWarehouse savedClothWarehouse=clothWarehouseRepository.save(clothWarehouse);
        return savedClothWarehouse;
    }

    public List<ClothWarehouse> getAllClothWarehouses(){
        return clothWarehouseRepository.findAll();
    }
    public List<ClothWarehouse> getAllClothWarehousesByClothId(UUID id){
        Cloth cloth=clothRepository.findById(id).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        List<ClothWarehouse> clothWarehouseList=clothWarehouseRepository.findAllByCloth(cloth).orElseThrow(()->new ClothWarehouseNotFoundException("Склад тканей не найдены"));
        return clothWarehouseList;
    }
    public ClothWarehouse getClothWarehouseById(Long id){
        ClothWarehouse clothWarehouse=clothWarehouseRepository.findById(id).orElseThrow(()->new ClothWarehouseNotFoundException("Склад тканей не найден"));
        return clothWarehouse;
    }
    public ClothWarehouseResponseUpdate updateClothWarehouse(ClothWarehouseDTO clothWarehouseDTO){
        ClothWarehouse clothWarehouse=clothWarehouseRepository.findById(clothWarehouseDTO.getId()).orElseThrow(()->new ClothWarehouseNotFoundException("Склад тканей не найден"));
        return goodResponseUpdate("Склад ткани успешно обновлён",clothWarehouseFacade.clothWarehouseDTOToClothWarehouse(clothWarehouseDTO));
    }

    public ClothWarehouseResponseDelete deleteClothWarehouse(Long id){
        ClothWarehouse clothWarehouse=clothWarehouseRepository.findById(id).orElseThrow(()->new ClothWarehouseNotFoundException("Склад тканей не найден"));
        return goodResponseDelete("склад ткани успешно удалён",id);
    }
    private Long generateRandomNumberId(){
        Random random=new Random();
        int max=100000;
        int min=1;
        long rndNum= random.nextInt(max - min) + min;
        List<ClothWarehouse> clothWarehouses=getAllClothWarehouses();
        for(int i=0;i<clothWarehouses.size();i++){
            ClothWarehouse clothWarehouse=clothWarehouses.get(i);
            if(clothWarehouse.getId().equals(rndNum)){
                generateRandomNumberId();
            }
        }
        Long res = (Long) rndNum;
        return res;
    }
    private ClothWarehouseResponseUpdate goodResponseUpdate(String msg,ClothWarehouse clothWarehouse){
        ClothWarehouse updatedClothWarehouse=clothWarehouseRepository.save(clothWarehouse);
        ClothWarehouseResponseUpdate response=new ClothWarehouseResponseUpdate();
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage(msg);
        response.setBody(clothWarehouseFacade.clothWarehouseToClothWarehouseDTO(updatedClothWarehouse));
        return response;
    }

    private ClothWarehouseResponseDelete goodResponseDelete(String msg,Long id){
        clothWarehouseRepository.deleteById(id);
        ClothWarehouseResponseDelete response=new ClothWarehouseResponseDelete();
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage(msg);
        return response;
    }
}