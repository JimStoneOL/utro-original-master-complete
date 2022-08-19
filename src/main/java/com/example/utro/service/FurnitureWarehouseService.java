package com.example.utro.service;

import com.example.utro.dto.FurnitureWarehouseDTO;
import com.example.utro.entity.Furniture;
import com.example.utro.entity.FurnitureWarehouse;
import com.example.utro.exceptions.FurnitureNotFoundException;
import com.example.utro.exceptions.FurnitureWarehouseNotFoundException;
import com.example.utro.facade.FurnitureWarehouseFacade;
import com.example.utro.payload.response.FurnitureWarehouseResponseDelete;
import com.example.utro.payload.response.FurnitureWarehouseResponseUpdate;
import com.example.utro.repository.FurnitureRepository;
import com.example.utro.repository.FurnitureWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class FurnitureWarehouseService {
    private final FurnitureWarehouseRepository furnitureWarehouseRepository;
    private final FurnitureWarehouseFacade furnitureWarehouseFacade;
    private final FurnitureRepository furnitureRepository;

    @Autowired
    public FurnitureWarehouseService(FurnitureWarehouseRepository furnitureWarehouseRepository, FurnitureWarehouseFacade furnitureWarehouseFacade, FurnitureRepository furnitureRepository) {
        this.furnitureWarehouseRepository = furnitureWarehouseRepository;
        this.furnitureWarehouseFacade = furnitureWarehouseFacade;
        this.furnitureRepository = furnitureRepository;
    }


    public FurnitureWarehouse createFurnitureWarehouse(FurnitureWarehouseDTO furnitureWarehouseDTO){
        furnitureWarehouseDTO.setId(generateRandomNumberId());
        FurnitureWarehouse furnitureWarehouse=furnitureWarehouseFacade.furnitureWarehouseDTOToFurnitureWarehouse(furnitureWarehouseDTO);
        FurnitureWarehouse savedFurnitureWarehouse=furnitureWarehouseRepository.save(furnitureWarehouse);
        return savedFurnitureWarehouse;
    }

    public List<FurnitureWarehouse> getAllFurnitureWarehouses(){
        List<FurnitureWarehouse> furnitureWarehouseList=furnitureWarehouseRepository.findAll();
        return furnitureWarehouseList;
    }
    public List<FurnitureWarehouse> getAllFurnitureWarehousesByFurnitureId(UUID id){
        Furniture furniture=furnitureRepository.findById(id).orElseThrow(()->new FurnitureNotFoundException("Фурнитура не найдена"));
        List<FurnitureWarehouse> furnitureWarehouseList=furnitureWarehouseRepository.findAllByFurniture(furniture).orElseThrow(()->new FurnitureWarehouseNotFoundException("Склад фурнитуры не найдены"));
        return furnitureWarehouseList;
    }
    public FurnitureWarehouse getFurnitureWarehouseById(Long id){
        FurnitureWarehouse furnitureWarehouse=furnitureWarehouseRepository.findById(id).orElseThrow(()->new FurnitureWarehouseNotFoundException("склад фурнитуры не найден"));
        return furnitureWarehouse;
    }
    public FurnitureWarehouseResponseUpdate updateFurnitureWarehouse(FurnitureWarehouseDTO furnitureWarehouseDTO){
        FurnitureWarehouse furnitureWarehouse=furnitureWarehouseRepository.findById(furnitureWarehouseDTO.getId()).orElseThrow(()->new FurnitureWarehouseNotFoundException("склад фурнитуры не найден"));
        return goodResponseUpdate("Склад фурнитуры успешно обновлён",furnitureWarehouseFacade.furnitureWarehouseDTOToFurnitureWarehouse(furnitureWarehouseDTO));
    }
    public FurnitureWarehouseResponseDelete deleteFurnitureWarehouse(Long id) {
        FurnitureWarehouse furnitureWarehouse = furnitureWarehouseRepository.findById(id).orElseThrow(() -> new FurnitureWarehouseNotFoundException("склад фурнитуры не найден"));
        return goodResponseDelete("Склад фурнитуры успешно удалён",id);
    }

    private FurnitureWarehouseResponseUpdate goodResponseUpdate(String msg, FurnitureWarehouse furnitureWarehouse){
        FurnitureWarehouse updatedFurnitureWarehouse=furnitureWarehouseRepository.save(furnitureWarehouse);
        FurnitureWarehouseResponseUpdate response=new FurnitureWarehouseResponseUpdate();
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage(msg);
        response.setBody(furnitureWarehouseFacade.furnitureWarehouseToFurnitureWarehouseDTO(updatedFurnitureWarehouse));
        return response;
    }

    private FurnitureWarehouseResponseDelete goodResponseDelete(String msg,Long id){
        furnitureWarehouseRepository.deleteById(id);
        FurnitureWarehouseResponseDelete response=new FurnitureWarehouseResponseDelete();
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage(msg);
        return response;
    }
    public Long generateRandomNumberId(){
        int max=100000;
        int min=1;
        Random random=new Random();
        long rndNum= random.nextInt(max - min) + min;
        List<FurnitureWarehouse> furnitureWarehouses=getAllFurnitureWarehouses();
        for(int i=0;i<furnitureWarehouses.size();i++){
            FurnitureWarehouse furnitureWarehouse=furnitureWarehouses.get(i);
            if(furnitureWarehouse.getId().equals(rndNum)){
                generateRandomNumberId();
            }
        }
        Long res = (Long) rndNum;
        return res;
    }
}