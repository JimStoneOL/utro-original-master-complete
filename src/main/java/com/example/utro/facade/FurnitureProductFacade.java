package com.example.utro.facade;

import com.example.utro.dto.FurnitureProductDTO;
import com.example.utro.entity.FurnitureProduct;
import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import com.example.utro.repository.FurnitureRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FurnitureProductFacade {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FurnitureRepository furnitureRepository;

    public FurnitureProduct furnitureProductDTOtoFurnitureProduct(FurnitureProductDTO furnitureProductDTO){
        FurnitureProduct furnitureProduct=new FurnitureProduct();
        furnitureProduct.setId(furnitureProductDTO.getId());
        //Рассмотреть вариант возможности выбрасывать exception-ы
        furnitureProduct.setProduct(productRepository.findById(furnitureProductDTO.getProductId()).get());
        furnitureProduct.setFurniture(furnitureRepository.findById(furnitureProductDTO.getFurnitureId()).get());
        furnitureProduct.setPlacement(furnitureProductDTO.getPlacement());
        furnitureProduct.setWidth(furnitureProductDTO.getWidth());
        furnitureProduct.setLength(furnitureProductDTO.getLength());
        furnitureProduct.setTurn(furnitureProductDTO.getTurn());
        furnitureProduct.setAmount(furnitureProductDTO.getAmount());
        return  furnitureProduct;
    }
    public FurnitureProductDTO furnitureProductToFurnitureProductDTO(FurnitureProduct furnitureProduct){
        FurnitureProductDTO furnitureProductDTO=new FurnitureProductDTO();
        furnitureProductDTO.setId(furnitureProduct.getId());
        furnitureProductDTO.setProductId(furnitureProduct.getProduct().getArticle());
        furnitureProductDTO.setFurnitureId(furnitureProduct.getFurniture().getArticle());
        furnitureProductDTO.setPlacement(furnitureProduct.getPlacement());
        furnitureProductDTO.setWidth(furnitureProduct.getWidth());
        furnitureProductDTO.setLength(furnitureProduct.getLength());
        furnitureProductDTO.setTurn(furnitureProduct.getTurn());
        furnitureProductDTO.setAmount(furnitureProduct.getAmount());
        return furnitureProductDTO;
    }
    public List<FurnitureProductDTO> furnitureProductListToFurnitureProductDTOList(List<FurnitureProduct> furnitureProductList){
        List<FurnitureProductDTO> furnitureProductDTOList=new ArrayList<>();
        for(int i=0;i<furnitureProductList.size();i++){
            FurnitureProductDTO furnitureProductDTO=new FurnitureProductDTO();
            furnitureProductDTO.setId(furnitureProductList.get(i).getId());
            furnitureProductDTO.setProductId(furnitureProductList.get(i).getProduct().getArticle());
            furnitureProductDTO.setFurnitureId(furnitureProductList.get(i).getFurniture().getArticle());
            furnitureProductDTO.setPlacement(furnitureProductList.get(i).getPlacement());
            furnitureProductDTO.setWidth(furnitureProductList.get(i).getWidth());
            furnitureProductDTO.setLength(furnitureProductList.get(i).getLength());
            furnitureProductDTO.setTurn(furnitureProductList.get(i).getTurn());
            furnitureProductDTO.setAmount(furnitureProductList.get(i).getAmount());
            furnitureProductDTOList.add(furnitureProductDTO);
        }
        return furnitureProductDTOList;
    }
    public FurnitureProduct template(FurnitureProduct furnitureProduct, User user){
        FurnitureProduct templateFurnitureProduct=new FurnitureProduct();
        templateFurnitureProduct.setProduct(furnitureProduct.getProduct());
        templateFurnitureProduct.setFurniture(furnitureProduct.getFurniture());
        templateFurnitureProduct.setPlacement(furnitureProduct.getPlacement());
        templateFurnitureProduct.setWidth(furnitureProduct.getWidth());
        templateFurnitureProduct.setLength(furnitureProduct.getLength());
        templateFurnitureProduct.setTurn(furnitureProduct.getTurn());
        templateFurnitureProduct.setAmount(furnitureProduct.getAmount());
        templateFurnitureProduct.setUser(user);
        return templateFurnitureProduct;
    }
    public FurnitureProduct templateToProduct(FurnitureProduct furnitureProduct, Product product, User user){
        FurnitureProduct templateFurnitureProduct=new FurnitureProduct();
        templateFurnitureProduct.setProduct(product);
        templateFurnitureProduct.setFurniture(furnitureProduct.getFurniture());
        templateFurnitureProduct.setPlacement(furnitureProduct.getPlacement());
        templateFurnitureProduct.setWidth(furnitureProduct.getWidth());
        templateFurnitureProduct.setLength(furnitureProduct.getLength());
        templateFurnitureProduct.setTurn(furnitureProduct.getTurn());
        templateFurnitureProduct.setAmount(furnitureProduct.getAmount());
        templateFurnitureProduct.setUser(user);
        return templateFurnitureProduct;
    }
}