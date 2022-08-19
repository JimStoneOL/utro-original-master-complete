package com.example.utro.facade;


import com.example.utro.dto.OrderDTO;
import com.example.utro.entity.Order;
import com.example.utro.entity.OrderedProduct;
import com.example.utro.entity.User;
import com.example.utro.exceptions.OrderedProductNotFoundException;
import com.example.utro.payload.request.CustomerOrderRequest;
import com.example.utro.payload.response.OrderResponse;
import com.example.utro.repository.OrderedProductRepository;
import com.example.utro.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderFacade {
    @Autowired
    private OrderedProductRepository orderedProductRepository;

    public Order orderDTOToOrder(OrderDTO orderDTO){
        Order order=new Order();
        order.setStage(orderDTO.getStage());
        List<Long> orderedProductsId= orderDTO.getOrderedProducts();
        List<OrderedProduct> orderedProductList=new ArrayList<>();
        for(int i=0;i<orderedProductsId.size();i++){
            OrderedProduct orderedProduct=orderedProductRepository.findById(orderedProductsId.get(i)).orElseThrow(()->new OrderedProductNotFoundException("Заказынные изделия не найдены"));
            orderedProductList.add(orderedProduct);
        }
        order.setOrderedProducts(orderedProductList);
        return  order;
    }
    public OrderDTO orderToOrderDTO(Order order){
        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setStage(order.getStage());
        orderDTO.setPrice(order.getPrice());
        List<String> userList=new ArrayList<>();
        for(int i=0;i<order.getUser().size();i++){
            userList.add(order.getUser().get(i).getEmail());
        }
        orderDTO.setUsername(userList);
        List<OrderedProduct> orderedProducts=order.getOrderedProducts();
        if(orderedProducts!=null) {
            List<Long> orderedProductsId = new ArrayList<>();
            for (int i = 0; i < orderedProducts.size(); i++) {
                orderedProductsId.add(orderedProducts.get(i).getId());
            }
            orderDTO.setOrderedProducts(orderedProductsId);
        }else{
            orderDTO.setOrderedProducts(null);
        }
        return orderDTO;
    }
    public List<OrderDTO> ordersListToOrderDTOList(List<Order> orders){
        List<OrderDTO> orderDTOList=new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            OrderDTO orderDTO=new OrderDTO();
            orderDTO.setId(orders.get(i).getId());
            orderDTO.setStage(orders.get(i).getStage());
            orderDTO.setPrice(orders.get(i).getPrice());
            List<OrderedProduct> orderedProducts=orders.get(i).getOrderedProducts();
            List<Long> orderedProductsId=new ArrayList<>();
            for(int k=0;k<orderedProducts.size();k++){
                orderedProductsId.add(orderedProducts.get(k).getId());
            }
            orderDTO.setOrderedProducts(orderedProductsId);
            List<User> userList=orders.get(i).getUser();
            List<String> emails=new ArrayList<>();
            for(int k=0;k<userList.size();k++){
                String username=userList.get(k).getEmail();
                emails.add(username);
            }
            orderDTO.setUsername(emails);
            orderDTOList.add(orderDTO);

        }
        return orderDTOList;
    }
    public Order customerOrderRequestToOrder(CustomerOrderRequest customerOrderRequest){
        Order order=new Order();
        order.setId(customerOrderRequest.getId());
        List<Long> orderedProductsId=customerOrderRequest.getOrderedProducts();
        List<OrderedProduct> orderedProductList=new ArrayList<>();
        order.setOrderedProducts(orderedProductList);
        return order;
    }
}