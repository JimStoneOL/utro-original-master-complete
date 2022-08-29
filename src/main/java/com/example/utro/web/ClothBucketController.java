package com.example.utro.web;

import com.example.utro.dto.ClothDTO;
import com.example.utro.entity.Cloth;
import com.example.utro.entity.ClothBucket;
import com.example.utro.exceptions.ClothBucketNotFoundException;
import com.example.utro.facade.ClothFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.ClothBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/cloth/bucket")
@CrossOrigin
public class ClothBucketController {

    @Autowired
    private ClothBucketService clothBucketService;

    @Autowired
    private ClothFacade clothFacade;


    @PostMapping("/create/{clothId}")
    public ResponseEntity<Object> createClothBucket(@PathVariable("clothId") UUID clothId, Principal principal){
        ClothBucket clothBucket=clothBucketService.createClothBucket(clothId,principal);
        return new ResponseEntity<>(clothBucket,HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllClothByUser(Principal principal){
        List<Cloth> clothList=clothBucketService.getAllClothByUser(principal);
        List<ClothDTO> clothDTOList=clothFacade.clothListToClothDTOList(clothList);
        return new ResponseEntity<>(clothDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/{clothId}")
    public ResponseEntity<Object> getClothByClothIdAndUser(@PathVariable("clothId") UUID clothId,Principal principal){
        Cloth cloth;
        try{
            cloth=clothBucketService.getClothByClothIdAndUser(clothId,principal);
        }catch (ClothBucketNotFoundException e){
            return new ResponseEntity<>(new MessageResponse("Ткань не добавлена в корзину"), HttpStatus.OK);
        }
        ClothDTO clothDTO=clothFacade.clothToClothDTO(cloth);
        return new ResponseEntity<>(clothDTO,HttpStatus.OK);
    }

    @PostMapping("/delete/{clothId}")
    public ResponseEntity<Object> deleteClothBucketByClothId(@PathVariable("clothId") UUID clothId,Principal principal){
        MessageResponse messageResponse=clothBucketService.deleteClothBucket(clothId,principal);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @PostMapping("/delete/all")
    public ResponseEntity<Object> deleteAllClothBucket(Principal principal){
        MessageResponse messageResponse=clothBucketService.deleteAllClothBucket(principal);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}
