package com.example.utro.facade;

import com.example.utro.dto.OrderedProductDTO;
import com.example.utro.entity.Order;
import com.example.utro.entity.OrderedProduct;
import com.example.utro.entity.Product;
import com.example.utro.exceptions.OrderNotFoundException;
import com.example.utro.exceptions.ProductNotFoundException;
import com.example.utro.repository.OrderRepository;
import com.example.utro.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderedProductFacade {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderedProduct orderedProductDTOToOrderedProduct(OrderedProductDTO orderedProductDTO){
        OrderedProduct orderedProduct=new OrderedProduct();
        Order order=orderRepository.findById(orderedProductDTO.getOrderId()).orElseThrow(()->new OrderNotFoundException("заказ не найден"));
        orderedProduct.setOrder(order);
        Product product=productRepository.findById(orderedProductDTO.getProductId()).orElseThrow(()->new ProductNotFoundException("Продукт не найден"));
        orderedProduct.setProduct(product);
        orderedProduct.setAmount(orderedProductDTO.getAmount());
        return orderedProduct;
    }
    public OrderedProductDTO orderedProductToOrderedProductDTO(OrderedProduct orderedProduct){
        OrderedProductDTO orderedProductDTO=new OrderedProductDTO();
        orderedProductDTO.setId(orderedProduct.getId());
        orderedProductDTO.setOrderId(orderedProduct.getOrder().getId());
        orderedProductDTO.setProductId(orderedProduct.getProduct().getArticle());
        orderedProductDTO.setAmount(orderedProduct.getAmount());
        return orderedProductDTO;
    }
    public List<OrderedProductDTO> orderedProductListToOrderedProductDTOList(List<OrderedProduct> orderedProducts){
        List<OrderedProductDTO> orderedProductDTOList=new ArrayList<>();
        for(int i=0;i<orderedProducts.size();i++){
            OrderedProduct orderedProduct=orderedProducts.get(i);
            OrderedProductDTO orderedProductDTO=new OrderedProductDTO();
            orderedProductDTO.setId(orderedProduct.getId());
            orderedProductDTO.setOrderId(orderedProduct.getOrder().getId());
            orderedProductDTO.setProductId(orderedProduct.getProduct().getArticle());
            orderedProductDTO.setAmount(orderedProduct.getAmount());
            orderedProductDTOList.add(orderedProductDTO);
        }
        return orderedProductDTOList;
    }
}
