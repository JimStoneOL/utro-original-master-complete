package com.example.utro.facade;

import com.example.utro.dto.ClothProductDTO;
import com.example.utro.entity.ClothProduct;
import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import com.example.utro.repository.ClothRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ClothProductFacade {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClothRepository clothRepository;

    public ClothProduct clothProductDTOtoClothProduct(ClothProductDTO clothProductDTO){
        ClothProduct clothProduct=new ClothProduct();
        clothProduct.setId(clothProductDTO.getId());
        clothProduct.setProduct(productRepository.findById(clothProductDTO.getProductId()).get());
        clothProduct.setCloth(clothRepository.findById(clothProductDTO.getClothId()).get());
        return clothProduct;
    }
    public ClothProductDTO clothProductToClothProductDTO(ClothProduct clothProduct){
        ClothProductDTO clothProductDTO=new ClothProductDTO();
        clothProductDTO.setId(clothProduct.getId());
        clothProductDTO.setProductId(clothProduct.getProduct().getArticle());
        clothProductDTO.setClothId(clothProduct.getCloth().getArticle());
        return clothProductDTO;
    }
    public List<ClothProductDTO> clothProductListToClothProductDTOList(List<ClothProduct> clothProductList){
        List<ClothProductDTO> clothProductDTOList=new ArrayList<>();
        for(int i=0;i<clothProductList.size();i++){
            ClothProductDTO clothProductDTO=new ClothProductDTO();
            clothProductDTO.setId(clothProductList.get(i).getId());
            clothProductDTO.setProductId(clothProductList.get(i).getProduct().getArticle());
            clothProductDTO.setClothId(clothProductList.get(i).getCloth().getArticle());
            clothProductDTOList.add(clothProductDTO);
        }
        return clothProductDTOList;
    }
    public ClothProduct template(ClothProduct clothProduct, User user){
        ClothProduct templateClothProduct=new ClothProduct();
        templateClothProduct.setCloth(clothProduct.getCloth());
        templateClothProduct.setProduct(clothProduct.getProduct());
        templateClothProduct.setUser(user);
        return templateClothProduct;
    }

    public ClothProduct templateToProduct(ClothProduct clothProduct, Product product, User user){
        ClothProduct templateClothProduct=new ClothProduct();
        templateClothProduct.setCloth(clothProduct.getCloth());
        templateClothProduct.setProduct(product);
        templateClothProduct.setUser(user);
        return templateClothProduct;
    }
}