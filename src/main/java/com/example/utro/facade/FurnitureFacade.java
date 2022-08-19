package com.example.utro.facade;

import com.example.utro.dto.FurnitureDTO;
import com.example.utro.entity.Furniture;
import com.example.utro.entity.FurnitureWarehouse;
import com.example.utro.entity.User;
import com.example.utro.repository.FurnitureWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class FurnitureFacade {

    @Autowired
    private FurnitureWarehouseRepository furnitureWarehouseRepository;

    public Furniture furnitureDTOtoFurniture(FurnitureDTO furnitureDTO){
        Furniture furniture=new Furniture();
        furniture.setArticle(furnitureDTO.getArticle());
        furniture.setName(furnitureDTO.getName());
        furniture.setType(furnitureDTO.getType());
        furniture.setWidth(furnitureDTO.getWidth());
        furniture.setLength(furnitureDTO.getLength());
        furniture.setWeight(furnitureDTO.getWeight());
        furniture.setPrice(furnitureDTO.getPrice());
        if(furnitureDTO.getFurnitureWarehousesId()!=null) {
            List<FurnitureWarehouse> furnitureWarehouseList = new ArrayList<>();
            for (int i = 0; i < furnitureDTO.getFurnitureWarehousesId().size(); i++) {
                Optional<FurnitureWarehouse> isFurnitureWarehouse = furnitureWarehouseRepository.findById(furnitureDTO.getFurnitureWarehousesId().get(i));
                if (isFurnitureWarehouse.isPresent()) {
                    furnitureWarehouseList.add(isFurnitureWarehouse.get());
                }
            }
            furniture.setFurnitureWarehouses(furnitureWarehouseList);
        }
        return furniture;
    }
    public FurnitureDTO furnitureToFurnitureDTO(Furniture furniture){
        FurnitureDTO furnitureDTO=new FurnitureDTO();
        furnitureDTO.setArticle(furniture.getArticle());
        furnitureDTO.setName(furniture.getName());
        furnitureDTO.setType(furniture.getType());
        furnitureDTO.setWidth(furniture.getWidth());
        furnitureDTO.setLength(furniture.getLength());
        furnitureDTO.setWeight(furniture.getWeight());
        furnitureDTO.setPrice(furniture.getPrice());
        if(furniture.getFurnitureWarehouses()!=null) {
            List<Long> furnitureWarehousesId = new ArrayList<>();
            for (int i = 0; i < furniture.getFurnitureWarehouses().size(); i++) {
                furnitureWarehousesId.add(furniture.getFurnitureWarehouses().get(i).getId());
            }
            furnitureDTO.setFurnitureWarehousesId(furnitureWarehousesId);
        }
        return furnitureDTO;
    }
    public List<FurnitureDTO> furnitureListToFurnitureDTOList(List<Furniture> furnitureList){
        List<FurnitureDTO> furnitureDTOList=new ArrayList<>();
        for(int i=0;i<furnitureList.size();i++){
            FurnitureDTO furnitureDTO=new FurnitureDTO();
            furnitureDTO.setArticle(furnitureList.get(i).getArticle());
            furnitureDTO.setName(furnitureList.get(i).getName());
            furnitureDTO.setType(furnitureList.get(i).getType());
            furnitureDTO.setWidth(furnitureList.get(i).getWidth());
            furnitureDTO.setLength(furnitureList.get(i).getLength());
            furnitureDTO.setWeight(furnitureList.get(i).getWeight());
            furnitureDTO.setPrice(furnitureList.get(i).getPrice());
            if(furnitureList.get(i).getFurnitureWarehouses()!=null) {
                List<Long> furnitureWarehousesId = new ArrayList<>();
                for (int k = 0; k < furnitureList.get(i).getFurnitureWarehouses().size(); k++) {
                    furnitureWarehousesId.add(furnitureList.get(i).getFurnitureWarehouses().get(k).getId());
                }
                furnitureDTO.setFurnitureWarehousesId(furnitureWarehousesId);
            }
            furnitureDTOList.add(furnitureDTO);
        }
        return furnitureDTOList;
    }
}