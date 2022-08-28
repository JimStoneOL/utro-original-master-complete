package com.example.utro.service;

import com.example.utro.entity.*;
import com.example.utro.exceptions.*;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.FurnitureBucketRepository;
import com.example.utro.repository.FurnitureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FurnitureBucketService {

    private final FurnitureBucketRepository furnitureBucketRepository;
    private final PrincipalService principalService;

    private final FurnitureRepository furnitureRepository;


    @Autowired
    public FurnitureBucketService(FurnitureBucketRepository furnitureBucketRepository, PrincipalService principalService, FurnitureRepository furnitureRepository) {
        this.furnitureBucketRepository = furnitureBucketRepository;
        this.principalService = principalService;
        this.furnitureRepository = furnitureRepository;
    }


    public FurnitureBucket createFurnitureBucket(UUID furnitureId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<FurnitureBucket> furnitureBucketList=furnitureBucketRepository.findAllByUserId(user.getId()).orElse(null);
        if(furnitureBucketList!=null){
            if(furnitureBucketList.size()>=5){
                throw new FurnitureBucketOutOfRangeException("Фурнитур больше или равно 5");
            }
        }
        furnitureRepository.findById(furnitureId).orElseThrow(()->new FurnitureNotFoundException("Фурнитура не найдена"));
        FurnitureBucket furnitureBucket=new FurnitureBucket();
        furnitureBucket.setUserId(user.getId());
        furnitureBucket.setFurnitureId(furnitureId);
        FurnitureBucket savedFurnitureBucket=furnitureBucketRepository.save(furnitureBucket);
        return savedFurnitureBucket;
    }

    public List<Furniture> getAllFurnitureByUser(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<FurnitureBucket> furnitureBucketList=furnitureBucketRepository.findAllByUserId(user.getId()).orElse(null);
        if(furnitureBucketList==null){
            throw new FurnitureBucketNotFoundException("Корзина фурнитур не найдена");
        }

        List<Furniture> furnitureList=new ArrayList<>();

        for(int i=0;i<furnitureBucketList.size();i++){
            FurnitureBucket furnitureBucket=furnitureBucketList.get(i);
            Furniture furniture=furnitureRepository.findById(furnitureBucket.getFurnitureId()).orElse(null);
            if (furniture!=null){
                furnitureList.add(furniture);
            }
        }
        return furnitureList;
    }

    public Furniture getFurnitureByFurnitureIdAndUser(UUID furnitureId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureBucket furnitureBucket=furnitureBucketRepository.findByFurnitureIdAndUserId(furnitureId, user.getId()).orElseThrow(()->new FurnitureBucketNotFoundException("Корзина фурнитур не найдена"));
        Furniture furniture=furnitureRepository.findById(furnitureBucket.getFurnitureId()).orElseThrow(()->new FurnitureNotFoundException("Фурнитура не найдена"));
        return furniture;
    }

    public MessageResponse deleteFurnitureBucket(UUID furnitureId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureBucket furnitureBucket=furnitureBucketRepository.findByFurnitureIdAndUserId(furnitureId, user.getId()).orElseThrow(()->new FurnitureBucketNotFoundException("Корзина фурнитур не найдена"));
        furnitureBucketRepository.deleteById(furnitureBucket.getId());
        return new MessageResponse("Успешно удалено");
    }

    public MessageResponse deleteAllFurnitureBucket(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<FurnitureBucket> furnitureBucketList=furnitureBucketRepository.findAllByUserId(user.getId()).orElseThrow(()->new FurnitureBucketNotFoundException("Корзина фурнитур не найдена"));
        for(int i=0;i<furnitureBucketList.size();i++){
            furnitureBucketRepository.deleteById(furnitureBucketList.get(i).getId());
        }
        return new MessageResponse("Успешно удалено");
    }
}