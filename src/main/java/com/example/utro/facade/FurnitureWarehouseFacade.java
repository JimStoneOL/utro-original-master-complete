package com.example.utro.facade;

import com.example.utro.dto.FurnitureWarehouseDTO;
import com.example.utro.entity.FurnitureWarehouse;
import com.example.utro.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FurnitureWarehouseFacade {

    @Autowired
    private FurnitureRepository furnitureRepository;

    public FurnitureWarehouse furnitureWarehouseDTOToFurnitureWarehouse(FurnitureWarehouseDTO furnitureWarehouseDTO){
        FurnitureWarehouse furnitureWarehouse=new FurnitureWarehouse();
        furnitureWarehouse.setId(furnitureWarehouseDTO.getId());
        //Рассмотреть вариант выброс ошибок
        furnitureWarehouse.setFurniture(furnitureRepository.findById(furnitureWarehouseDTO.getFurnitureId()).get());
        furnitureWarehouse.setAmount(furnitureWarehouseDTO.getAmount());
        return furnitureWarehouse;
    }
    public FurnitureWarehouseDTO furnitureWarehouseToFurnitureWarehouseDTO(FurnitureWarehouse furnitureWarehouse){
        FurnitureWarehouseDTO furnitureWarehouseDTO=new FurnitureWarehouseDTO();
        furnitureWarehouseDTO.setId(furnitureWarehouse.getId());
        furnitureWarehouseDTO.setFurnitureId(furnitureWarehouse.getFurniture().getArticle());
        furnitureWarehouseDTO.setAmount(furnitureWarehouse.getAmount());
        return furnitureWarehouseDTO;
    }
    public List<FurnitureWarehouseDTO> furnitureWarehouseListToFurnitureWarehouseDTOList(List<FurnitureWarehouse> furnitureWarehouseList){
        List<FurnitureWarehouseDTO> furnitureWarehouseDTOList=new ArrayList<>();
        for(int i=0;i<furnitureWarehouseList.size();i++){
            FurnitureWarehouseDTO furnitureWarehouseDTO=new FurnitureWarehouseDTO();
            furnitureWarehouseDTO.setId(furnitureWarehouseList.get(i).getId());
            furnitureWarehouseDTO.setFurnitureId(furnitureWarehouseList.get(i).getFurniture().getArticle());
            furnitureWarehouseDTO.setAmount(furnitureWarehouseList.get(i).getAmount());
            furnitureWarehouseDTOList.add(furnitureWarehouseDTO);
        }
        return furnitureWarehouseDTOList;
    }
}