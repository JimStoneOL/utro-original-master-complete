package com.example.utro.service;

import com.example.utro.dto.OrderedProductDTO;
import com.example.utro.entity.*;
import com.example.utro.entity.enums.EStage;
import com.example.utro.exceptions.*;
import com.example.utro.facade.OrderedProductFacade;
import com.example.utro.payload.response.OrderedProductResponseDelete;
import com.example.utro.payload.response.OrderedProductResponseUpdate;
import com.example.utro.repository.OrderRepository;
import com.example.utro.repository.OrderedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class OrderedProductService {
    private final OrderedProductFacade orderedProductFacade;
    private final OrderedProductRepository orderedProductRepository;
    private final PrincipalService principalService;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderedProductService(OrderedProductFacade orderedProductFacade, OrderedProductRepository orderedProductRepository, PrincipalService principalService, OrderRepository orderRepository) {
        this.orderedProductFacade = orderedProductFacade;
        this.orderedProductRepository = orderedProductRepository;
        this.principalService = principalService;
        this.orderRepository = orderRepository;
    }
    public OrderedProduct createOrderedProduct(OrderedProductDTO orderedProductDTO, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        OrderedProduct orderedProduct=orderedProductFacade.orderedProductDTOToOrderedProduct(orderedProductDTO);
        orderedProduct.setUser(user);
        OrderedProduct savedOrderedProduct=orderedProductRepository.save(orderedProduct);
        return savedOrderedProduct;
    }
    public OrderedProduct getOrderedProductById(Long id,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        OrderedProduct orderedProduct=orderedProductRepository.findByIdAndUser(id,user).orElseThrow(()->new OrderedProductNotFoundException("заказанный продукт не найдены"));
        return orderedProduct;
    }
    public List<OrderedProduct> getAllOrderedProducts(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<OrderedProduct> orderedProductList=orderedProductRepository.findAllByUser(user).orElseThrow(()->new OrderedProductListNotFoundException("заказанные продукты не найдены"));
        return orderedProductList;
    }
    public OrderedProduct getAnyOrderedProductById(Long id){
        OrderedProduct orderedProduct=orderedProductRepository.findById(id).orElseThrow(()->new OrderedProductNotFoundException("заказанный продукт не найдены"));
        return orderedProduct;
    }
    public List<OrderedProduct> getAnyAllOrderedProducts(){
        List<OrderedProduct> orderedProductList=orderedProductRepository.findAll();
        return orderedProductList;
    }
    public OrderedProductResponseUpdate updateOrderedProduct(OrderedProductDTO orderedProductDTO,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        OrderedProduct orderedProduct=orderedProductRepository.findByIdAndUser(orderedProductDTO.getId(),user).orElseThrow(()->new OrderedProductNotFoundException("заказанный продукт не найдены"));
        Order order=orderedProduct.getOrder();
        if(order.getStage().equals(EStage.STAGE_NEW)){
            orderedProduct.setUser(user);
            OrderedProduct updatedOrderedProduct=orderedProductRepository.save(orderedProductFacade.orderedProductDTOToOrderedProduct(orderedProductDTO));
            OrderedProductDTO response=orderedProductFacade.orderedProductToOrderedProductDTO(updatedOrderedProduct);
            OrderedProductResponseUpdate orderedProductResponseUpdate=new OrderedProductResponseUpdate();
            orderedProductResponseUpdate.setHttpStatus(HttpStatus.OK);
            orderedProductResponseUpdate.setBody(response);
            orderedProductResponseUpdate.setMessage("Заказанные продукты обновлены");
            return orderedProductResponseUpdate;
        }else{
            OrderedProductResponseUpdate orderedProductResponseUpdate=new OrderedProductResponseUpdate();
            orderedProductResponseUpdate.setBody(null);
            orderedProductResponseUpdate.setMessage("Ошибка! Данный заказ находится в стадии "+order.getStage());
            orderedProductResponseUpdate.setHttpStatus(HttpStatus.BAD_REQUEST);
            return orderedProductResponseUpdate;
        }
    }
    public OrderedProductResponseDelete deleteOrderedProduct(Long id,Principal principal){
        OrderedProduct orderedProduct=getOrderedProductById(id,principal);
        Order order=orderedProduct.getOrder();
        if(order.getStage().equals(EStage.STAGE_NEW)){
            orderedProductRepository.deleteById(id);
            OrderedProductResponseDelete orderedProductResponseDelete=new OrderedProductResponseDelete();
            orderedProductResponseDelete.setHttpStatus(HttpStatus.OK);
            orderedProductResponseDelete.setMessage("Заказанный продукт успешно удалён");
            return orderedProductResponseDelete;
        }else{
            OrderedProductResponseDelete orderedProductResponseDelete=new OrderedProductResponseDelete();
            orderedProductResponseDelete.setHttpStatus(HttpStatus.BAD_REQUEST);
            orderedProductResponseDelete.setMessage("Ошибка! Данный заказ находиться в стадии "+order.getStage());
            return orderedProductResponseDelete;
        }
    }
    public List<OrderedProduct> getOrderedProductListByOrderId(UUID id, Principal principal) {
        User user=principalService.getUserByPrincipal(principal);
        Order order=orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Заказ не найден"));
        List<OrderedProduct> orderedProductList=orderedProductRepository.findAllByOrderAndUser(order,user).orElseThrow(()->new OrderedProductListNotFoundException("Заказанные продукты не найдены"));
        return orderedProductList;
    }

    public List<OrderedProduct> getAnyOrderedProductListByOrderId(UUID id) {
        Order order=orderRepository.findById(id).orElseThrow(()->new OrderNotFoundException("Заказ не найден"));
        List<OrderedProduct> orderedProductList=orderedProductRepository.findAllByOrder(order).orElseThrow(()->new OrderedProductListNotFoundException("Заказанные продукты не найдены"));
        return orderedProductList;
    }
}
