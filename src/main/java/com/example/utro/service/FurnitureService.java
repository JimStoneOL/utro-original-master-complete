package com.example.utro.service;

import com.example.utro.dto.FurnitureDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.FurnitureNotFoundException;
import com.example.utro.facade.FurnitureFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FurnitureService {
    private final FurnitureRepository furnitureRepository;
    private final FurnitureFacade furnitureFacade;
    private final FurnitureProductRepository furnitureProductRepository;
    private final ImageRepository imageRepository;
    private final FurnitureBucketRepository furnitureBucketRepository;
    private final FurnitureWarehouseRepository furnitureWarehouseRepository;

    @Autowired
    public FurnitureService(FurnitureRepository furnitureRepository, FurnitureFacade furnitureFacade, FurnitureProductRepository furnitureProductRepository, ImageRepository imageRepository, FurnitureBucketRepository furnitureBucketRepository, FurnitureWarehouseRepository furnitureWarehouseRepository) {
        this.furnitureRepository = furnitureRepository;
        this.furnitureFacade = furnitureFacade;
        this.furnitureProductRepository = furnitureProductRepository;
        this.imageRepository = imageRepository;
        this.furnitureBucketRepository = furnitureBucketRepository;
        this.furnitureWarehouseRepository = furnitureWarehouseRepository;
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
    public MessageResponse deleteFurniture(UUID furnitureId){
        Furniture furniture=furnitureRepository.findById(furnitureId).orElseThrow(()->new FurnitureNotFoundException("Фурнитура не найдена"));
        List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByFurniture(furniture).orElse(null);
        if(furnitureProductList!=null){
            for(int i=0;i<furnitureProductList.size();i++){
                furnitureProductRepository.deleteById(furnitureProductList.get(i).getId());
            }
        }
        ImageModel imageModel=imageRepository.findByFurnitureId(furnitureId).orElse(null);
        if(imageModel!=null){
            imageRepository.deleteById(imageModel.getId());
        }
        FurnitureBucket furnitureBucket=furnitureBucketRepository.findByFurnitureId(furnitureId).orElse(null);
        if(furnitureBucket!=null){
            furnitureBucketRepository.deleteById(furnitureBucket.getId());
        }
        List<FurnitureWarehouse> furnitureWarehouseList=furnitureWarehouseRepository.findAllByFurniture(furniture).orElse(null);
        if(furnitureWarehouseList!=null){
            for(int i=0;i<furnitureWarehouseList.size();i++){
                furnitureWarehouseRepository.deleteById(furnitureWarehouseList.get(i).getId());
            }
        }
        furnitureRepository.deleteById(furnitureId);
        return new MessageResponse("Фурнитура успешно удалена");
    }
}
