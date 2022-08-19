package com.example.utro.service;

import com.example.utro.dto.ClothProductDTO;
import com.example.utro.dto.FurnitureProductDTO;
import com.example.utro.dto.OrderDTO;
import com.example.utro.dto.ProductDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.OrderedProductNotFoundException;
import com.example.utro.facade.ClothProductFacade;
import com.example.utro.facade.FurnitureProductFacade;
import com.example.utro.facade.OrderFacade;
import com.example.utro.facade.ProductFacade;
import com.example.utro.repository.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    public double clothProductPrice(ClothProduct clothProduct){
        Cloth cloth=clothProduct.getCloth();
        Product product=clothProduct.getProduct();
        double squareMeter=cloth.getPrice()/(cloth.getWidth()*cloth.getLength());
        double result=0;
        result+=product.getLength()*product.getWidth()* squareMeter;
        return result;
    }
    public double furnitureProductPrice(FurnitureProduct furnitureProduct){
        Furniture furniture=furnitureProduct.getFurniture();
        Product product=furnitureProduct.getProduct();
        double meter=furniture.getPrice()/(furniture.getWidth()*furniture.getLength());
        double result=furnitureProduct.getLength()*furnitureProduct.getWidth()*meter;
        return result;
    }
    public double productPrice(Product product){
        List<ClothProduct> clothProduct=product.getClothProducts();
        double cloth=0;
        for(int i=0;i<clothProduct.size();i++) {
            cloth += clothProductPrice(clothProduct.get(i));
        }
        List<FurnitureProduct> furnitureProduct = product.getFurnitureProducts();
        double furniture=0;
        for(int i=0;i<furnitureProduct.size();i++){
            furniture += furnitureProductPrice(furnitureProduct.get(i));
        }
        return cloth+furniture;
    }
    public double orderPrice(List<OrderedProduct> orderedProducts){
        double result=0;
        for(int i=0;i<orderedProducts.size();i++){
            OrderedProduct orderedProduct=orderedProducts.get(i);
            Product product=orderedProduct.getProduct();
            int amount=orderedProduct.getAmount();
            double productPrice=productPrice(product);
            double oneRes=amount*productPrice;
            result+=oneRes;
        }
        return result;
    }
}