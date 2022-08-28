package com.example.utro.service;

import com.example.utro.dto.ClothProductDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.ClothProductListNotFoundException;
import com.example.utro.exceptions.ClothProductNotFoundException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.facade.ClothProductFacade;
import com.example.utro.payload.response.ClothProductResponseDelete;
import com.example.utro.payload.response.ClothProductResponseUpdate;
import com.example.utro.payload.response.FurnitureProductResponseDelete;
import com.example.utro.repository.ClothProductRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClothProductService {
    private final ClothProductRepository clothProductRepository;
    private final PrincipalService principalService;
    private final ClothProductFacade clothProductFacade;

    private final ProductRepository productRepository;

    @Autowired
    public ClothProductService(ClothProductRepository clothProductRepository, PrincipalService principalService, ClothProductFacade clothProductFacade, ProductRepository productRepository) {
        this.clothProductRepository = clothProductRepository;
        this.principalService = principalService;
        this.clothProductFacade = clothProductFacade;
        this.productRepository = productRepository;
    }
    public ClothProduct createClothProduct(ClothProductDTO clothProductDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothProduct clothProduct=clothProductFacade.clothProductDTOtoClothProduct(clothProductDTO);
        clothProduct.setUser(user);
        ClothProduct savedClothProduct=clothProductRepository.save(clothProduct);
        return savedClothProduct;
    }
    public List<ClothProduct> getAllClothProduct(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<ClothProduct> clothProductList=clothProductRepository.findAllByUser(user).orElseThrow(()->new ClothProductListNotFoundException("Ткани продуктов не найдены"));
        return clothProductList;
    }
    public ClothProduct getClothProductById(Long id,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothProduct clothProduct=clothProductRepository.findByIdAndUser(id,user).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        return clothProduct;
    }
    public ClothProductResponseUpdate updateClothProduct(ClothProductDTO clothProductDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothProduct clothProduct=clothProductRepository.findByIdAndUser(clothProductDTO.getId(),user).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        Product product=clothProduct.getProduct();
        List<Order> orders=user.getOrders();
        if(findProductInOrder(product,orders)){
            ClothProductResponseUpdate response=new ClothProductResponseUpdate();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Продукт находиться в заказе");
            response.setBody(null);
            return response;
        }else{
            ClothProduct updatedClothProduct=clothProductFacade.clothProductDTOtoClothProduct(clothProductDTO);
            updatedClothProduct.setUser(user);
            ClothProduct clothProductResponse=clothProductRepository.save(updatedClothProduct);
            ClothProductResponseUpdate response=new ClothProductResponseUpdate();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Ткань продукта успешно обновлена");
            response.setBody(clothProductFacade.clothProductToClothProductDTO(clothProductResponse));
            return response;
        }
    }
    public ClothProductResponseDelete deleteClothProduct(Long id, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothProduct clothProduct=clothProductRepository.findByIdAndUser(id,user).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        Product product=clothProduct.getProduct();
        List<Order> orders=user.getOrders();
        if(findProductInOrder(product,orders)){
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Продукт находиться в заказе");
            return response;
        }else{
            clothProductRepository.deleteById(id);
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Ткань продукта успещно удалена");
            return response;
        }
    }
    public ClothProduct createTemplateClothProduct(ClothProductDTO clothProductDTO){
        ClothProduct clothProduct=clothProductFacade.clothProductDTOtoClothProduct(clothProductDTO);
        ClothProduct savedClothProduct;
        Product product=clothProduct.getProduct();
        if(product.getUser()==null){
            savedClothProduct=clothProductRepository.save(clothProduct);
        }else{
            throw new ClothProductNotFoundException("Данная ткань продукта не является шаблонным");
        }
        return savedClothProduct;
    }
    public ClothProduct addTemplateClothProduct(Long id,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        ClothProduct clothProduct=clothProductRepository.findById(id).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        if(clothProduct.getUser()!=null){
            throw new ClothProductNotFoundException("Ткань продукта не найдена");
        }
        ClothProduct template=clothProductFacade.template(clothProduct,user);
        ClothProduct savedTemplateClothProduct=clothProductRepository.save(template);
        return savedTemplateClothProduct;
    }
    public ClothProduct getTemplateClothProductById(Long id){
        ClothProduct clothProduct=clothProductRepository.findById(id).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        if(clothProduct.getUser()==null){
            return clothProduct;
        }else{
            throw new ClothProductNotFoundException("Ткань продукта не найдена");
        }
    }
    public ClothProduct getAnyClothProductById(Long id){
        ClothProduct clothProduct=clothProductRepository.findById(id).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
            return clothProduct;
    }
    public List<ClothProduct> getTemplateClothProductListByProductId(UUID id) {
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        for(int i=0;i<clothProductList.size();i++){
            ClothProduct clothProduct=clothProductList.get(i);
            if(clothProduct.getUser()!=null){
                throw new ClothProductNotFoundException(clothProduct.getId()+" не является шаблонной тканью продукта");
            }
        }
        return clothProductList;
    }
    public ClothProductResponseDelete deleteAllClothProductByTemplateProductId(UUID productId){
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Данный продукт не является шаблонным");
            return response;
        }else{
            List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElseThrow(()->new ClothProductNotFoundException("Ткани продукта не найдена"));
            for (int i=0;i<clothProductList.size();i++){
                clothProductRepository.deleteById(clothProductList.get(i).getId());
            }
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Ткани продукта успешно удалены");
            return response;
        }
    }
    public ClothProductResponseDelete deleteAllClothProductByProductId(UUID productId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(user.getOrders()!=null) {
            boolean isFound = findProductInOrder(product, user.getOrders());
            if (isFound) {
                ClothProductResponseDelete response=new ClothProductResponseDelete();
                response.setHttpStatus(HttpStatus.BAD_REQUEST);
                response.setMessage("Ошибка! Данный продукт находиться в списке заказов");
                return response;
            }
        }
            List<ClothProduct> clothProductList=clothProductRepository.findAllByProductAndUser(product,user).orElseThrow(()->new ClothProductNotFoundException("Ткани продукта не найдена"));
            for (int i=0;i<clothProductList.size();i++){
                clothProductRepository.deleteById(clothProductList.get(i).getId());
            }
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Ткани продукта успешно удалены");
            return response;
    }
    public List<ClothProduct> getClothProductListByProductId(UUID id,Principal principal) {
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<ClothProduct> clothProductList=clothProductRepository.findAllByProductAndUser(product,user).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        return clothProductList;
    }
    public List<ClothProduct> getAnyClothProductListByProductId(UUID id) {
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        return clothProductList;
    }
    public List<ClothProduct> getAllTemplateClothProduct(){
        List<ClothProduct> clothProducts=clothProductRepository.findAll();
        List<ClothProduct> responseClothProducts=new ArrayList<>();
        for(int i=0;i<clothProducts.size();i++){
            if(clothProducts.get(i).getUser()==null){
                responseClothProducts.add(clothProducts.get(i));
            }
        }
        return responseClothProducts;
    }
    public List<ClothProduct> getAnyAllClothProduct(){
        List<ClothProduct> clothProducts=clothProductRepository.findAll();
        return clothProducts;
    }
    public ClothProductResponseUpdate updateTemplateClothProduct(ClothProductDTO clothProductDTO){
        ClothProduct clothProduct=clothProductRepository.findById(clothProductDTO.getId()).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        if(clothProduct!=null){
            ClothProductResponseUpdate response=new ClothProductResponseUpdate();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Данная ткань продукта не является шаблонным");
            response.setBody(null);
            return response;
        }else{
            ClothProduct updatedClothProduct=clothProductFacade.clothProductDTOtoClothProduct(clothProductDTO);
            ClothProduct clothProductResponse=clothProductRepository.save(updatedClothProduct);
            ClothProductResponseUpdate response=new ClothProductResponseUpdate();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Ткань продукта успешно обновлён");
            response.setBody(clothProductFacade.clothProductToClothProductDTO(clothProductResponse));
            return response;
        }
    }
    public ClothProductResponseDelete deleteTemplateClothProduct(Long id){
        ClothProduct clothProduct=clothProductRepository.findById(id).orElseThrow(()->new ClothProductNotFoundException("Ткань продукта не найдена"));
        if(clothProduct.getUser()!=null){
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Данная ткань продукта не является шаблонным");
            return response;
        }else{
            clothProductRepository.deleteById(id);
            ClothProductResponseDelete response=new ClothProductResponseDelete();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Данная ткань продукта успешно удалена");
            return response;
        }
    }
    private boolean findProductInOrder(Product product, List<Order> orders){

        for(int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            List<OrderedProduct> orderedProducts=order.getOrderedProducts();
            if(orderedProducts!=null) {
                for (int k = 0; k < orderedProducts.size(); k++) {
                    OrderedProduct orderedProduct = orderedProducts.get(k);
                    if (orderedProduct.getProduct().getArticle()==product.getArticle()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
