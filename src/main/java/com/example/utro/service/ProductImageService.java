package com.example.utro.service;


import com.example.utro.entity.*;
import com.example.utro.exceptions.InOrderException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.repository.OrderedProductRepository;
import com.example.utro.repository.ProductImageRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
public class ProductImageService {
    private final ProductImageRepository productImageRepository;
    private final PrincipalService principalService;
    private final ProductRepository productRepository;
    private final OrderedProductRepository orderedProductRepository;

    @Autowired
    public ProductImageService(ProductImageRepository productImageRepository, PrincipalService principalService, ProductRepository productRepository, OrderedProductRepository orderedProductRepository) {
        this.productImageRepository = productImageRepository;
        this.principalService = principalService;
        this.productRepository = productRepository;
        this.orderedProductRepository = orderedProductRepository;
    }

    public ProductImage uploadImageProduct(MultipartFile file, Principal principal, UUID productId) throws IOException {
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        boolean isFound=findProductInOrder(product,user.getOrders());
        if(isFound){
            throw new InOrderException("Продукт находится в заказе");
        }
        ProductImage productImage=new ProductImage();
        productImage.setProductId(product.getArticle());
        productImage.setImageBytes(compressBytes(file.getBytes()));
        productImage.setName(file.getOriginalFilename());
        return productImageRepository.save(productImage);
    }

    public ProductImage uploadImageTemplateProduct(MultipartFile file, UUID productId) throws IOException {
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            throw new ProductNotFoundException("Продукт не является шаблонным");
        }
        ProductImage productImage=new ProductImage();
        productImage.setProductId(product.getArticle());
        productImage.setImageBytes(compressBytes(file.getBytes()));
        productImage.setName(file.getOriginalFilename());
        return productImageRepository.save(productImage);
    }

    public List<ProductImage> getImageProduct(UUID productId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        List<ProductImage> productImageList=productImageRepository.findAllByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(productImageList)) {
            for(int i=0;i<productImageList.size();i++){
                ProductImage productImage=productImageList.get(i);
                productImage.setImageBytes(decompressBytes(productImage.getImageBytes()));
            }
        }
        return productImageList;
    }

    public List<ProductImage> getTemplateImageProduct(UUID productId){

        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            throw new ProductNotFoundException("Продукт не является шаблонным");
        }

        List<ProductImage> productImageList=productImageRepository.findAllByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(productImageList)) {
            for(int i=0;i<productImageList.size();i++){
                ProductImage productImage=productImageList.get(i);
                productImage.setImageBytes(decompressBytes(productImage.getImageBytes()));
            }
        }
        return productImageList;
    }

    public List<ProductImage> getAllImageProduct(UUID productId){

        List<ProductImage> productImageList=productImageRepository.findAllByProductId(productId).orElse(null);
        if (!ObjectUtils.isEmpty(productImageList)) {
            for(int i=0;i<productImageList.size();i++){
                ProductImage productImage=productImageList.get(i);
                productImage.setImageBytes(decompressBytes(productImage.getImageBytes()));
            }
        }
        return productImageList;
    }

    public MessageResponse deleteImageProduct(Long id,UUID productId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Product product=productRepository.findByArticleAndUser(productId,user).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        boolean isFound=findProductInOrder(product,user.getOrders());
        if(isFound){
            throw new InOrderException("Продукт находится в заказе");
        }
        productImageRepository.deleteById(id);
        return new MessageResponse("Успешно удалено");
    }

    public MessageResponse deleteTemplateImageProduct(Long id,UUID productId){
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        if(product.getUser()!=null){
            throw new ProductNotFoundException("Продукт не является шаблонным");
        }
        productImageRepository.deleteById(id);
        return new MessageResponse("Успешно удалено");
    }

    public void templateProductImage(UUID oldProductId,UUID newProductId){

        List<ProductImage> productImageList=productImageRepository.findAllByProductId(oldProductId).orElse(null);
        if (!ObjectUtils.isEmpty(productImageList)) {
            for(int i=0;i<productImageList.size();i++){

                ProductImage oldProductImage=productImageList.get(i);
                ProductImage newProductImage=new ProductImage();

                newProductImage.setProductId(newProductId);
                newProductImage.setImageBytes(oldProductImage.getImageBytes());
                newProductImage.setName(oldProductImage.getName());

                productImageRepository.save(newProductImage);
            }
        }
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {

        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {

        }
        return outputStream.toByteArray();
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
}
