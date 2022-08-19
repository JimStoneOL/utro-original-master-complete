package com.example.utro.facade;

import com.example.utro.dto.ProductDTO;
import com.example.utro.entity.*;
import com.example.utro.repository.ClothProductRepository;
import com.example.utro.repository.FurnitureProductRepository;
import com.example.utro.repository.ImageRepository;
import com.example.utro.repository.ProductRepository;
import com.example.utro.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductFacade {
    @Autowired
    private ClothFacade clothFacade;
    @Autowired
    private FurnitureFacade furnitureFacade;
    @Autowired
    private ClothProductFacade clothProductFacade;
    @Autowired
    private FurnitureProductFacade furnitureProductFacade;
    @Autowired
    private FurnitureProductRepository furnitureProductRepository;

    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClothProductRepository clothProductRepository;
    public Product productDTOtoProduct(ProductDTO productDTO){
        Product product=new Product();
        product.setArticle(productDTO.getArticle());
        product.setName(productDTO.getName());
        product.setWidth(productDTO.getWidth());
        product.setLength(productDTO.getLength());
        product.setComment(productDTO.getComment());
        return product;
    }
    public ProductDTO productToProductDTO(Product product){
        ProductDTO productDTO=new ProductDTO();
        productDTO.setArticle(product.getArticle());
        productDTO.setName(product.getName());
        productDTO.setWidth(product.getWidth());
        productDTO.setLength(product.getLength());
        productDTO.setComment(product.getComment());
        return productDTO;
    }

    public List<ProductDTO> productListToProductDTOList(List<Product> products){
        List<ProductDTO> productDTOList=new ArrayList<>();
        for(int i=0;i<products.size();i++) {
            ProductDTO productDTO=new ProductDTO();
            productDTO.setArticle(products.get(i).getArticle());
            productDTO.setName(products.get(i).getName());
            productDTO.setWidth(products.get(i).getWidth());
            productDTO.setLength(products.get(i).getLength());
            productDTO.setComment(products.get(i).getComment());
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }
    public Product template(Product product, User user) {
        Product templateProduct = new Product();
        templateProduct.setArticle(UUID.randomUUID());
        templateProduct.setUser(user);
        templateProduct.setName(product.getName());
        templateProduct.setWidth(product.getWidth());
        templateProduct.setLength(product.getLength());
        templateProduct.setComment(product.getComment());
        Product savedTemplateProduct=productRepository.save(templateProduct);
        //получили его артикль


        List<ClothProduct> clothProducts=product.getClothProducts();
        for(int i=0;i<clothProducts.size();i++){
            ClothProduct clothProduct=clothProducts.get(i);
            if(clothProduct.getUser()==null){
                //тут должны передать новый артикль продукта и старый артикль ткани
                ClothProduct templateClothProduct=clothProductFacade.templateToProduct(clothProduct,savedTemplateProduct,user);
                clothProductRepository.save(templateClothProduct);

            }
        }
        List<FurnitureProduct> furnitureProducts=product.getFurnitureProducts();
        for(int i=0;i<furnitureProducts.size();i++){
            FurnitureProduct furnitureProduct=furnitureProducts.get(i);
            if(furnitureProduct.getUser()==null){
                FurnitureProduct templateFurnitureProduct=furnitureProductFacade.templateToProduct(furnitureProduct,savedTemplateProduct,user);
                furnitureProductRepository.save(templateFurnitureProduct);
            }
        }
         imageUploadService.templateProductImage(product.getArticle(),savedTemplateProduct.getArticle());
        return templateProduct;
    }
}