package com.example.utro.facade;


import com.example.utro.dto.ClothWarehouseDTO;
import com.example.utro.entity.ClothWarehouse;
import com.example.utro.repository.ClothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClothWarehouseFacade {

    @Autowired
    private ClothRepository clothRepository;

    public ClothWarehouse clothWarehouseDTOToClothWarehouse(ClothWarehouseDTO clothWarehouseDTO){
        ClothWarehouse clothWarehouse=new ClothWarehouse();
        clothWarehouse.setId(clothWarehouseDTO.getId());
        clothWarehouse.setCloth(clothRepository.findById(clothWarehouseDTO.getClothId()).get());
        clothWarehouse.setLength(clothWarehouseDTO.getLength());
        return clothWarehouse;
    }
    public ClothWarehouseDTO clothWarehouseToClothWarehouseDTO(ClothWarehouse clothWarehouse){
        ClothWarehouseDTO clothWarehouseDTO=new ClothWarehouseDTO();
        clothWarehouseDTO.setId(clothWarehouse.getId());
        clothWarehouseDTO.setClothId(clothWarehouse.getCloth().getArticle());
        clothWarehouseDTO.setLength(clothWarehouse.getLength());
        return clothWarehouseDTO;
    }
    public List<ClothWarehouseDTO> clothWarehouseListToClothWarehouseDTOList(List<ClothWarehouse> clothWarehouseList){
        List<ClothWarehouseDTO> clothWarehouseDTOList=new ArrayList<>();
        for(int i=0;i<clothWarehouseList.size();i++){
            ClothWarehouseDTO clothWarehouseDTO=new ClothWarehouseDTO();
            clothWarehouseDTO.setId(clothWarehouseList.get(i).getId());
            clothWarehouseDTO.setClothId(clothWarehouseList.get(i).getCloth().getArticle());
            clothWarehouseDTO.setLength(clothWarehouseList.get(i).getLength());
            clothWarehouseDTOList.add(clothWarehouseDTO);
        }
        return  clothWarehouseDTOList;
    }
}