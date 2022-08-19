package com.example.utro.facade;


import com.example.utro.dto.ClothDTO;
import com.example.utro.entity.Cloth;
import com.example.utro.entity.ClothWarehouse;
import com.example.utro.repository.ClothWarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ClothFacade {

    @Autowired
    private ClothWarehouseRepository clothWarehouseRepository;

    public Cloth clothDTOtoCloth(ClothDTO clothDTO){
        Cloth cloth=new Cloth();
        cloth.setArticle(clothDTO.getArticle());
        cloth.setName(clothDTO.getName());
        cloth.setColor(clothDTO.getColor());
        cloth.setDrawing(clothDTO.getDrawing());
        cloth.setStructure(clothDTO.getStructure());
        cloth.setWidth(clothDTO.getWidth());
        cloth.setLength(clothDTO.getLength());
        cloth.setPrice(clothDTO.getPrice());
        if(clothDTO.getClothWarehousesId() != null) {
            List<ClothWarehouse> clothWarehouseList = new ArrayList<>();
            for (int i = 0; i < clothDTO.getClothWarehousesId().size(); i++) {
                Optional<ClothWarehouse> isClothWarehouse = clothWarehouseRepository.findById(clothDTO.getClothWarehousesId().get(i));
                if (isClothWarehouse.isPresent()) {
                    clothWarehouseList.add(isClothWarehouse.get());
                }
            }
            cloth.setClothWarehouses(clothWarehouseList);
        }
        return cloth;
    }
    public ClothDTO clothToClothDTO(Cloth cloth){
        ClothDTO clothDTO=new ClothDTO();
        clothDTO.setArticle(cloth.getArticle());
        clothDTO.setName(cloth.getName());
        clothDTO.setColor(cloth.getColor());
        clothDTO.setDrawing(cloth.getDrawing());
        clothDTO.setStructure(cloth.getStructure());
        clothDTO.setWidth(cloth.getWidth());
        clothDTO.setLength(cloth.getLength());
        clothDTO.setPrice(cloth.getPrice());
        if(cloth.getClothWarehouses() != null) {
            List<Long> clothWarehousesId = new ArrayList<>();
            for (int i = 0; i < cloth.getClothWarehouses().size(); i++) {
                clothWarehousesId.add(cloth.getClothWarehouses().get(i).getId());
            }
            clothDTO.setClothWarehousesId(clothWarehousesId);
        }
        return clothDTO;
    }
    public List<ClothDTO> clothListToClothDTOList(List<Cloth> clothList){
        List<ClothDTO> clothDTOList=new ArrayList<>();
        for(int i=0;i<clothList.size();i++){
            ClothDTO clothDTO=new ClothDTO();
            clothDTO.setArticle(clothList.get(i).getArticle());
            clothDTO.setName(clothList.get(i).getName());
            clothDTO.setColor(clothList.get(i).getColor());
            clothDTO.setDrawing(clothList.get(i).getDrawing());
            clothDTO.setStructure(clothList.get(i).getStructure());
            clothDTO.setWidth(clothList.get(i).getWidth());
            clothDTO.setLength(clothList.get(i).getLength());
            clothDTO.setPrice(clothList.get(i).getPrice());
            if(clothList.get(i).getClothWarehouses() != null) {
                List<Long> clothWarehousesId = new ArrayList<>();
                for (int k = 0; k < clothList.get(i).getClothWarehouses().size(); k++) {
                    clothWarehousesId.add(clothList.get(i).getClothWarehouses().get(k).getId());
                }
                clothDTO.setClothWarehousesId(clothWarehousesId);
            }

            clothDTOList.add(clothDTO);
        }
        return clothDTOList;
    }
}