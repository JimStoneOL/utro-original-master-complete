package com.example.utro.service;

import com.example.utro.dto.FurnitureProductDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.ClothProductNotFoundException;
import com.example.utro.exceptions.FurnitureProductListNotFoundException;
import com.example.utro.exceptions.FurnitureProductNotFoundException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.facade.FurnitureProductFacade;
import com.example.utro.payload.response.FurnitureProductResponseDelete;
import com.example.utro.payload.response.FurnitureProductResponseUpdate;
import com.example.utro.repository.FurnitureProductRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FurnitureProductService {
    private final FurnitureProductRepository furnitureProductRepository;
    private final PrincipalService principalService;
    private final FurnitureProductFacade furnitureProductFacade;
    private final ProductRepository productRepository;

    @Autowired
    public FurnitureProductService(FurnitureProductRepository furnitureProductRepository, PrincipalService principalService, FurnitureProductFacade furnitureProductFacade, ProductRepository productRepository) {
        this.furnitureProductRepository = furnitureProductRepository;
        this.principalService = principalService;
        this.furnitureProductFacade = furnitureProductFacade;
        this.productRepository = productRepository;
    }


    public FurnitureProduct createFurnitureProduct(FurnitureProductDTO furnitureProductDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureProduct furnitureProduct=furnitureProductFacade.furnitureProductDTOtoFurnitureProduct(furnitureProductDTO);
        furnitureProduct.setUser(user);
        FurnitureProduct savedFurnitureProduct=furnitureProductRepository.save(furnitureProduct);
        return savedFurnitureProduct;
    }
    public List<FurnitureProduct> getAllFurnitureProduct(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByUser(user).orElseThrow(()->new FurnitureProductListNotFoundException("Фурнитура продуктов не найдены"));
        return furnitureProductList;
    }
    public FurnitureProduct getFurnitureProductById(Long id,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureProduct furnitureProduct=furnitureProductRepository.findByIdAndUser(id,user).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        return furnitureProduct;
    }
    public FurnitureProductResponseUpdate updateFurnitureProduct(FurnitureProductDTO furnitureProductDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureProduct furnitureProduct=furnitureProductRepository.findByIdAndUser(furnitureProductDTO.getId(),user).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        Product product=furnitureProduct.getProduct();
        List<Order> orders=user.getOrders();
        if(findProductInOrder(product,orders)){
            FurnitureProductResponseUpdate response=new FurnitureProductResponseUpdate();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Продукт находиться в заказе");
            response.setBody(null);
            return response;
        }else{
            FurnitureProduct updatedFurnitureProduct=furnitureProductFacade.furnitureProductDTOtoFurnitureProduct(furnitureProductDTO);
            updatedFurnitureProduct.setUser(user);
            FurnitureProduct furnitureProductResponse=furnitureProductRepository.save(updatedFurnitureProduct);
            FurnitureProductResponseUpdate response=new FurnitureProductResponseUpdate();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Фурнитура продукта успешно обновлена");
            response.setBody(furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProductResponse));
            return response;
        }
    }
    public FurnitureProductResponseDelete deleteFurnitureProduct(Long id, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        FurnitureProduct furnitureProduct=furnitureProductRepository.findByIdAndUser(id,user).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        Product product=furnitureProduct.getProduct();
        List<Order> orders=user.getOrders();
        if(findProductInOrder(product,orders)){
            FurnitureProductResponseDelete response=new FurnitureProductResponseDelete();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Продукт находиться в заказе");
            return response;
        }else{
            furnitureProductRepository.deleteById(id);
            FurnitureProductResponseDelete response=new FurnitureProductResponseDelete();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Фурнитура продукта успещно удалена");
            return response;
        }
    }
    public FurnitureProduct createTemplateFurnitureProduct(FurnitureProductDTO furnitureProductDTO){
        FurnitureProduct furnitureProduct=furnitureProductFacade.furnitureProductDTOtoFurnitureProduct(furnitureProductDTO);
        FurnitureProduct savedFurnitureProduct;
        Product product=furnitureProduct.getProduct();
        if(product.getUser()==null){
            savedFurnitureProduct=furnitureProductRepository.save(furnitureProduct);
        }else{
            throw new FurnitureProductNotFoundException("Данная фурнитура продукта не является шаблонным");
        }
        return savedFurnitureProduct;
    }
        public FurnitureProduct getTemplateFurnitureProductById(Long id){
            FurnitureProduct furnitureProduct=furnitureProductRepository.findById(id).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
            if(furnitureProduct.getUser()==null){
                return furnitureProduct;
            }else{
                throw new FurnitureProductNotFoundException("Фурнитура продукта не найдена");
            }
        }

    public FurnitureProduct getAnyFurnitureProductById(Long id){
        FurnitureProduct furnitureProduct=furnitureProductRepository.findById(id).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
            return furnitureProduct;
    }

    public List<FurnitureProduct> getTemplateFurnitureProductListByProductId(UUID id) {
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProduct(product).orElseThrow(()->new ClothProductNotFoundException("Фурнитуры продукта не найдены"));
        for(int i=0;i<furnitureProductList.size();i++){
            FurnitureProduct furnitureProduct=furnitureProductList.get(i);
            if(furnitureProduct.getUser()!=null){
                throw new FurnitureProductNotFoundException(furnitureProduct.getId()+" не является шаблонной фурнитурой продукта");
            }
        }
        return furnitureProductList;
    }
        public List<FurnitureProduct> getAllTemplateFurnitureProduct(){
            List<FurnitureProduct> furnitureProducts=furnitureProductRepository.findAll();
            List<FurnitureProduct> responseFurnitureProducts=new ArrayList<>();
            for(int i=0;i<furnitureProducts.size();i++){
                if(furnitureProducts.get(i).getUser()==null){
                    responseFurnitureProducts.add(furnitureProducts.get(i));
                }
            }
        return responseFurnitureProducts;
        }
    public List<FurnitureProduct> getAnyAllFurnitureProduct(){
        List<FurnitureProduct> furnitureProducts=furnitureProductRepository.findAll();
        return furnitureProducts;
    }
    public List<FurnitureProduct> getFurnitureProductListByProductId(UUID id,Principal principal) {
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProductAndUser(product,user).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        return furnitureProductList;
    }

    public List<FurnitureProduct> getAnyFurnitureProductListByProductId(UUID id) {
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProduct(product).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        return furnitureProductList;
    }

        public FurnitureProductResponseUpdate updateTemplateFurnitureProduct(FurnitureProductDTO furnitureProductDTO){
        FurnitureProduct furnitureProduct=furnitureProductRepository.findById(furnitureProductDTO.getId()).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
        if(furnitureProduct!=null){
                FurnitureProductResponseUpdate response=new FurnitureProductResponseUpdate();
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Ошибка! Данная фурнитура продукта не является шаблонным");
                response.setBody(null);
                return response;
            }else{
                FurnitureProduct updatedFurnitureProduct=furnitureProductFacade.furnitureProductDTOtoFurnitureProduct(furnitureProductDTO);
                FurnitureProduct furnitureProductResponse=furnitureProductRepository.save(updatedFurnitureProduct);
                FurnitureProductResponseUpdate response=new FurnitureProductResponseUpdate();
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Фурнитура продукта успешно обавлена");
                response.setBody(furnitureProductFacade.furnitureProductToFurnitureProductDTO(furnitureProductResponse));
                return response;
            }
        }
        public FurnitureProductResponseDelete deleteTemplateFurnitureProduct(Long id){
            FurnitureProduct furnitureProduct=furnitureProductRepository.findById(id).orElseThrow(()->new FurnitureProductNotFoundException("Фурнитура продукта не найдена"));
            if(furnitureProduct.getUser()!=null){
                FurnitureProductResponseDelete response=new FurnitureProductResponseDelete();
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Ошибка! Данная фурнитура продукта не является шаблонным");
                return response;
            }else{
                furnitureProductRepository.deleteById(id);
                FurnitureProductResponseDelete response=new FurnitureProductResponseDelete();
                response.setHttpStatus(HttpStatus.OK);
                response.setMessage("Данная фурнитура продукта успешно удалена");
                return response;
            }
        }
    private boolean findProductInOrder(Product product, List<Order> orders){
        for(int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            List<OrderedProduct> orderedProducts=order.getOrderedProducts();
            for(int k=0;k<orderedProducts.size();k++){
                OrderedProduct orderedProduct=orderedProducts.get(i);
                if(orderedProduct.getProduct().getArticle().equals(product.getArticle())){
                    return true;
                }
            }
        }
        return false;
    }
}
