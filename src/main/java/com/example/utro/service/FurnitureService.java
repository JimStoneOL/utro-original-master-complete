package com.example.utro.service;

import com.example.utro.dto.FurnitureDTO;
import com.example.utro.entity.Furniture;
import com.example.utro.exceptions.FurnitureNotFoundException;
import com.example.utro.facade.FurnitureFacade;
import com.example.utro.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class FurnitureService {
    private final FurnitureRepository furnitureRepository;
    private final FurnitureFacade furnitureFacade;

    @Autowired
    public FurnitureService(FurnitureRepository furnitureRepository, FurnitureFacade furnitureFacade) {
        this.furnitureRepository = furnitureRepository;
        this.furnitureFacade = furnitureFacade;
    }
    public Furniture createFurniture(FurnitureDTO furnitureDTO){
        furnitureDTO.setArticle(UUID.randomUUID());
        Furniture furniture=furnitureFacade.furnitureDTOtoFurniture(furnitureDTO);
        Furniture savedFurniture=furnitureRepository.save(furniture);
        return savedFurniture;
    }
    public Furniture getFurnitureById(UUID article){
        Furniture furniture=furnitureRepository.findById(article).orElseThrow(()->new FurnitureNotFoundException("фурнитура не найдена"));
            return furniture;
    }
    public List<Furniture> getAllFurniture(){
        List<Furniture> furnitureList=furnitureRepository.findAll();
        return furnitureList;
    }
}
