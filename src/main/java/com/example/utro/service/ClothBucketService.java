package com.example.utro.service;

import com.example.utro.entity.Cloth;
import com.example.utro.entity.ClothBucket;
import com.example.utro.entity.ClothProduct;
import com.example.utro.entity.User;
import com.example.utro.exceptions.ClothBucketExistException;
import com.example.utro.exceptions.ClothBucketNotFoundException;
import com.example.utro.exceptions.ClothBucketOutOfRangeException;
import com.example.utro.exceptions.ClothNotFoundException;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.ClothBucketRepository;
import com.example.utro.repository.ClothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClothBucketService {

    private final ClothBucketRepository clothBucketRepository;
    private final PrincipalService principalService;
    private final ClothRepository clothRepository;


    @Autowired
    public ClothBucketService(ClothBucketRepository clothBucketRepository, PrincipalService principalService, ClothRepository clothRepository) {
        this.clothBucketRepository = clothBucketRepository;
        this.principalService = principalService;
        this.clothRepository = clothRepository;
    }

    public ClothBucket createClothBucket(UUID clothId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<ClothBucket> clothBucketList=clothBucketRepository.findAllByUserId(user.getId()).orElse(null);
        if(clothBucketList!=null){
            if(clothBucketList.size()>=5){
                throw new ClothBucketOutOfRangeException("Тканей больше или равно 5");
            }
        }

        clothRepository.findById(clothId).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        ClothBucket clothBucketIsExist=clothBucketRepository.findByClothIdAndUserId(clothId, user.getId()).orElse(null);
        if(clothBucketIsExist!=null){
            throw new ClothBucketExistException("Ткань уже добавлена в корзину");
        }
        ClothBucket clothBucket=new ClothBucket();
        clothBucket.setUserId(user.getId());
        clothBucket.setClothId(clothId);
        ClothBucket savedClothBucket=clothBucketRepository.save(clothBucket);
        return savedClothBucket;
    }

    public List<Cloth> getAllClothByUser(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<ClothBucket> clothBucketList=clothBucketRepository.findAllByUserId(user.getId()).orElse(null);
        if(clothBucketList==null){
            throw new ClothBucketNotFoundException("Корзина тканей не найдена");
        }
        List<Cloth> clothList=new ArrayList<>();
        for(int i=0;i<clothBucketList.size();i++){
            ClothBucket clothBucket=clothBucketList.get(i);
            Cloth cloth=clothRepository.findById(clothBucket.getClothId()).orElse(null);
            if (cloth!=null){
                clothList.add(cloth);
            }
        }
        return clothList;
    }

    public Cloth getClothByClothIdAndUser(UUID clothId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothBucket clothBucket=clothBucketRepository.findByClothIdAndUserId(clothId, user.getId()).orElseThrow(()->new ClothBucketNotFoundException("Корзина тканей не найдена"));
        Cloth cloth=clothRepository.findById(clothBucket.getClothId()).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        return cloth;
    }

    public MessageResponse deleteClothBucket(UUID clothId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothBucket clothBucket=clothBucketRepository.findByClothIdAndUserId(clothId, user.getId()).orElseThrow(()->new ClothBucketNotFoundException("Корзина тканей не найдена"));
        clothBucketRepository.deleteById(clothBucket.getId());
        return new MessageResponse("Успешно удалено");
    }

    public MessageResponse deleteAllClothBucket(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<ClothBucket> clothBucketList=clothBucketRepository.findAllByUserId(user.getId()).orElseThrow(()->new ClothBucketNotFoundException("Корзина тканей не найдена"));
        for(int i=0;i<clothBucketList.size();i++){
            clothBucketRepository.deleteById(clothBucketList.get(i).getId());
        }
        return new MessageResponse("Успешно удалено");
    }
}
