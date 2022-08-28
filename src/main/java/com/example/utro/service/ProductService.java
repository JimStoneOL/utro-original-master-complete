package com.example.utro.service;

import com.example.utro.dto.ProductDTO;
import com.example.utro.entity.*;
import com.example.utro.exceptions.ProductListNotFoundException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.facade.ProductFacade;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.ProductResponseDelete;
import com.example.utro.payload.response.ProductResponseUpdate;
import com.example.utro.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final PrincipalService principalService;
    private final ProductFacade productFacade;
    private final ClothProductRepository clothProductRepository;
    private final FurnitureProductRepository furnitureProductRepository;
    private final OrderRepository orderRepository;
    private final OrderedProductRepository orderedProductRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, PrincipalService principalService, ProductFacade productFacade, ClothProductRepository clothProductRepository, FurnitureProductRepository furnitureProductRepository, OrderRepository orderRepository, OrderedProductRepository orderedProductRepository) {
        this.productRepository = productRepository;
        this.principalService = principalService;
        this.productFacade = productFacade;
        this.clothProductRepository = clothProductRepository;
        this.furnitureProductRepository = furnitureProductRepository;
        this.orderRepository = orderRepository;
        this.orderedProductRepository = orderedProductRepository;
    }
    public Product createProduct(ProductDTO productDTO, Principal principal){
        productDTO.setArticle(UUID.randomUUID());
        User user=principalService.getUserByPrincipal(principal);
        Product product=productFacade.productDTOtoProduct(productDTO);
        product.setUser(user);
        return productRepository.save(product);
    }
    public Product createTemplateProduct(ProductDTO productDTO){
        productDTO.setArticle(UUID.randomUUID());
        Product product=productFacade.productDTOtoProduct(productDTO);
//        List<ClothProduct> clothProductList=product.getClothProducts();
//        for(int i=0;i<clothProductList.size();i++){
//            ClothProduct clothProduct=clothProductList.get(i);
//            if(clothProduct.getUser()!=null){
//                throw new ClothProductNotFoundException("Ткань продукта не является шаблонным");
//            }
//        }
//        List<FurnitureProduct> furnitureProductList=product.getFurnitureProducts();
//        for(int i=0;i<furnitureProductList.size();i++){
//            FurnitureProduct furnitureProduct=furnitureProductList.get(i);
//            if(furnitureProduct.getUser()!=null){
//                throw new FurnitureProductNotFoundException("Фурнитура продукта не является шаблонным");
//            }
//        }
        Product savedProduct=productRepository.save(product);
        return savedProduct;
    }
    public Product addTemplateProduct(UUID article, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findById(article).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser() != null){
            throw new ProductNotFoundException("Продукт не найден");
        }
        Product templateProduct=productFacade.template(product,user);
        Product savedTemplateProduct=productRepository.save(templateProduct);
        return savedTemplateProduct;
    }
    public Product getProductById(UUID article,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(article,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        return product;
    }
    public List<Product> getAllProducts(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<Product> products=productRepository.findAllByUser(user).orElseThrow(()->new ProductListNotFoundException("Продукты не найдены"));
        return products;
    }

    public ProductResponseDelete deleteProduct(UUID article, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(article,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<Order> orders=orderRepository.findAllByUser(user).orElse(null);
        if(orders!=null) {
            boolean isFound=findProductInOrder(product, orders);
            if (isFound) {
                ProductResponseDelete productResponseDelete = new ProductResponseDelete();
                productResponseDelete.setHttpStatus(HttpStatus.BAD_REQUEST);
                productResponseDelete.setMessage("Ошибка! Данный продукт находиться в списке заказов");
                return productResponseDelete;
            } else {
                List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElse(null);
                if(clothProductList!=null){
                    log.info(clothProductList.size()+"");
                    for(int i=0;i<clothProductList.size();i++){
                        ClothProduct clothProduct=clothProductList.get(i);
                        clothProductRepository.deleteById(clothProduct.getId());
                    }
                }
                List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProduct(product).orElse(null);
                if(furnitureProductList!=null){
                    for(int i=0;i<furnitureProductList.size();i++){
                        FurnitureProduct clothProduct=furnitureProductList.get(i);
                        furnitureProductRepository.deleteById(clothProduct.getId());
                    }
                }
                productRepository.deleteById(article);
                ProductResponseDelete productResponseDelete = new ProductResponseDelete();
                productResponseDelete.setHttpStatus(HttpStatus.OK);
                productResponseDelete.setMessage("Данный продукт успешно удалён");
                return productResponseDelete;
            }
        }else{
            List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElse(null);
            if(clothProductList!=null){
                log.info(clothProductList.size()+"");
                for(int i=0;i<clothProductList.size();i++){
                    ClothProduct clothProduct=clothProductList.get(i);
                    clothProductRepository.deleteById(clothProduct.getId());
                }
            }
            List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProduct(product).orElse(null);
            if(furnitureProductList!=null){
                for(int i=0;i<furnitureProductList.size();i++){
                    FurnitureProduct clothProduct=furnitureProductList.get(i);
                    furnitureProductRepository.deleteById(clothProduct.getId());
                }
            }
            productRepository.deleteById(article);
            ProductResponseDelete productResponseDelete = new ProductResponseDelete();
            productResponseDelete.setHttpStatus(HttpStatus.OK);
            productResponseDelete.setMessage("Данный продукт успешно удалён");
            return productResponseDelete;
        }
    }
    public ProductResponseUpdate updateProduct(ProductDTO productDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productDTO.getArticle(),user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<Order> orders=user.getOrders();
        if(findProductInOrder(product,orders)){
            ProductResponseUpdate response=new ProductResponseUpdate();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Данный продукт находиться в списке заказов");
            response.setBody(null);
            return response;
        }else{
            Product updatedProduct=productFacade.productDTOtoProduct(productDTO);
            updatedProduct.setUser(user);
            Product productResponse=productRepository.save(updatedProduct);
            ProductResponseUpdate response=new ProductResponseUpdate();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Продукт успешно обновлён");
            response.setBody(productFacade.productToProductDTO(productResponse));
            return response;
        }
    }
    public Product getTemplateProductById(UUID id){
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFoundException("продукт не найден"));
        if(product.getUser() == null){
            return product;
        }else{
            throw new ProductNotFoundException("продукт не найден");
        }
    }
    public List<Product> getAllTemplateProducts(){
        List<Product> products=productRepository.findAll();
        List<Product> responseProducts=new ArrayList<>();
        for(int i=0;i<products.size();i++){
            if(products.get(i).getUser() == null){
                responseProducts.add(products.get(i));
            }
        }
        return responseProducts;
    }
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
    public MessageResponse checkInOrder(UUID productId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        boolean isFound=findProductInOrder(product,user.getOrders());
        return new MessageResponse(isFound+"");
    }
    public Product getProductById(UUID article){
        return productRepository.findById(article).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
    }
    private boolean findProductInOrder(Product product, List<Order> orders){

        for(int i=0;i<orders.size();i++){
            Order order=orders.get(i);
            List<OrderedProduct> orderedProducts=orderedProductRepository.findAllByOrder(order).orElse(null);
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
    public ProductResponseDelete deleteTemplateProduct(UUID article){
        Product product=productRepository.findById(article).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            ProductResponseDelete productResponseDelete=new ProductResponseDelete();
            productResponseDelete.setHttpStatus(HttpStatus.BAD_REQUEST);
            productResponseDelete.setMessage("Ошибка! Данный продукт не является шаблонным");
            return productResponseDelete;
        }else{
            List<ClothProduct> clothProductList=clothProductRepository.findAllByProduct(product).orElse(null);
            if(clothProductList!=null){
                for(int i=0;i<clothProductList.size();i++){
                    ClothProduct clothProduct=clothProductList.get(i);
                    clothProductRepository.deleteById(clothProduct.getId());
                }
            }
            List<FurnitureProduct> furnitureProductList=furnitureProductRepository.findAllByProduct(product).orElse(null);
            if(furnitureProductList!=null){
                for(int i=0;i<furnitureProductList.size();i++){
                    FurnitureProduct clothProduct=furnitureProductList.get(i);
                    furnitureProductRepository.deleteById(clothProduct.getId());
                }
            }
            productRepository.deleteById(article);
            ProductResponseDelete productResponseDelete=new ProductResponseDelete();
            productResponseDelete.setHttpStatus(HttpStatus.OK);
            productResponseDelete.setMessage("Данный продукт успешно удалён");
            return productResponseDelete;
        }
    }
    public ProductResponseUpdate updateTemplateProduct(ProductDTO productDTO){
        Product product=productRepository.findById(productDTO.getArticle()).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            ProductResponseUpdate response=new ProductResponseUpdate();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.setMessage("Ошибка! Данный продукт не является шаблонным");
            response.setBody(null);
            return response;
        }else{
            Product updatedProduct=productFacade.productDTOtoProduct(productDTO);
            Product productResponse=productRepository.save(updatedProduct);
            ProductResponseUpdate response=new ProductResponseUpdate();
            response.setHttpStatus(HttpStatus.OK);
            response.setMessage("Продукт успешно обновлён");
            response.setBody(productFacade.productToProductDTO(productResponse));
            return response;
        }
    }

}
