package com.example.utro.service;


import com.example.utro.dto.ClothDTO;
import com.example.utro.entity.Cloth;
import com.example.utro.exceptions.ClothNotFoundException;
import com.example.utro.facade.ClothFacade;
import com.example.utro.repository.ClothRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClothService {
    private final ClothRepository clothRepository;
    private final ClothFacade clothFacade;

    @Autowired
    public ClothService(ClothRepository clothRepository, ClothFacade clothFacade) {
        this.clothRepository = clothRepository;
        this.clothFacade = clothFacade;
    }
    public Cloth createCloth(ClothDTO clothDTO){
        clothDTO.setArticle(UUID.randomUUID());
        Cloth cloth=clothFacade.clothDTOtoCloth(clothDTO);
        Cloth savedCloth=clothRepository.save(cloth);
        return savedCloth;
    }
    public Cloth getClothById(UUID article){
        Cloth cloth=clothRepository.findById(article).orElseThrow(()->new ClothNotFoundException("Ткань не найдена"));
        return cloth;
    }
    public List<Cloth> getAllCloth(){
        List<Cloth> clothList=clothRepository.findAll();
        return clothList;
    }


}