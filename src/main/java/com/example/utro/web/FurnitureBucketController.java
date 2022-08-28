package com.example.utro.web;

import com.example.utro.dto.ClothDTO;
import com.example.utro.dto.FurnitureDTO;
import com.example.utro.entity.Cloth;
import com.example.utro.entity.Furniture;
import com.example.utro.entity.FurnitureBucket;
import com.example.utro.exceptions.ClothBucketNotFoundException;
import com.example.utro.exceptions.FurnitureBucketNotFoundException;
import com.example.utro.facade.FurnitureFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.service.FurnitureBucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/furniture/bucket")
@CrossOrigin
public class FurnitureBucketController {

    @Autowired
    private FurnitureBucketService furnitureBucketService;

    @Autowired
    private FurnitureFacade furnitureFacade;


    @PostMapping("/create/{furnitureId}")
    public ResponseEntity<Object> createFurnitureBucket(@PathVariable("furnitureId") UUID furnitureId, Principal principal){
        FurnitureBucket furnitureBucket;
        try{
            furnitureBucket=furnitureBucketService.createFurnitureBucket(furnitureId,principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(furnitureBucket,HttpStatus.CREATED);
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAllFurnitureByUser(Principal principal){
        List<Furniture> furnitureList;
        try{
            furnitureList=furnitureBucketService.getAllFurnitureByUser(principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        List<FurnitureDTO> furnitureDTOList=furnitureFacade.furnitureListToFurnitureDTOList(furnitureList);
        return new ResponseEntity<>(furnitureDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/{furnitureId}")
    public ResponseEntity<Object> getFurnitureByFurnitureIdAndUser(@PathVariable("furnitureId") UUID furnitureId,Principal principal){
        Furniture furniture;
        try{
            furniture=furnitureBucketService.getFurnitureByFurnitureIdAndUser(furnitureId,principal);
        }catch (FurnitureBucketNotFoundException e){
            return new ResponseEntity<>(new MessageResponse("Фурнитура не добавлена в корзину"), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        FurnitureDTO furnitureDTO=furnitureFacade.furnitureToFurnitureDTO(furniture);
        return new ResponseEntity<>(furnitureDTO,HttpStatus.OK);
    }

    @PostMapping("/delete/{furnitureId}")
    public ResponseEntity<Object> deleteFurnitureBucket(@PathVariable("furnitureId") UUID furnitureId,Principal principal){
        MessageResponse messageResponse;
        try{
            messageResponse=furnitureBucketService.deleteFurnitureBucket(furnitureId,principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

    @PostMapping("/delete/all")
    public ResponseEntity<Object> deleteAllFurnitureBucket(Principal principal){
        MessageResponse messageResponse;
        try{
            messageResponse=furnitureBucketService.deleteAllFurnitureBucket(principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }
}

