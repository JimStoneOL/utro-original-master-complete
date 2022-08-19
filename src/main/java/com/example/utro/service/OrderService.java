package com.example.utro.service;

import com.example.utro.entity.Order;
import com.example.utro.entity.OrderedProduct;
import com.example.utro.entity.User;
import com.example.utro.entity.enums.EStage;
import com.example.utro.exceptions.OrderNotFoundException;
import com.example.utro.exceptions.OrderedProductNotFoundException;
import com.example.utro.exceptions.OrdersListNotFoundException;
import com.example.utro.facade.OrderFacade;
import com.example.utro.payload.request.CustomerOrderRequest;
import com.example.utro.payload.request.UpdateStageOrderRequest;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.OrderResponseDelete;
import com.example.utro.payload.response.OrderResponseUpdate;
import com.example.utro.repository.OrderRepository;
import com.example.utro.repository.OrderedProductRepository;
import com.example.utro.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final PrincipalService principalService;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderFacade orderFacade;
    private final UserRepository userRepository;
    private final PriceService priceService;

    @Autowired
    public OrderService(OrderRepository orderRepository, PrincipalService principalService, OrderedProductRepository orderedProductRepository, OrderFacade orderFacade, UserRepository userRepository, PriceService priceService) {
        this.orderRepository = orderRepository;
        this.principalService = principalService;
        this.orderedProductRepository = orderedProductRepository;
        this.orderFacade = orderFacade;
        this.userRepository = userRepository;
        this.priceService = priceService;
    }
    //customer
    public Order createOrder(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order order=new Order();
        order.setId(UUID.randomUUID());
        order.setPrice(0);
        List<User> userList=new ArrayList<>();
        userList.add(user);
        order.setUser(userList);
        order.setStage(EStage.STAGE_NEW);
        order.setOrderedProducts(null);
        Order savedOrder=orderRepository.save(order);
        List<Order> orders=user.getOrders();
        orders.add(savedOrder);
        user.setOrders(orders);
        userRepository.save(user);
        return savedOrder;
    }
    //customer and manager
    public Order getOrderById(UUID orderId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order order=orderRepository.findByIdAndUser(orderId,user).orElseThrow(()->new OrderNotFoundException("Order not found"));
        return order;
    }
    //customer and manager
    public List<Order> getAllOrders(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<Order> listOrders=orderRepository.findAllByUser(user).orElseThrow(()->new OrdersListNotFoundException("Orders not found"));
        return listOrders;
    }
    //Manager
    public List<Order> getAnyAllOrders(Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        List<Order> listOrders=orderRepository.findAll();
        List<Order> notTakenOrder=new ArrayList<>();
        for(int i=0;i<listOrders.size();i++){
            Order order=listOrders.get(i);
            Order isManagerConsistInOrder=orderRepository.findByIdAndUser(order.getId(),user).orElse(null);
            if(isManagerConsistInOrder == null){
                notTakenOrder.add(order);
            }
        }
        return notTakenOrder;
    }
    //manager
    public Order addManager(UUID orderId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order order=orderRepository.findById(orderId).orElseThrow(()->new OrderNotFoundException("Order not found"));
        List<User> userList=order.getUser();
        userList.add(user);
        order.setUser(userList);
        Order savedOrder=orderRepository.save(order);
        List<Order> orders=user.getOrders();
        orders.add(savedOrder);
        user.setOrders(orders);
        userRepository.save(user);
        return savedOrder;
    }
    public boolean checkManager(UUID orderId,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order check=orderRepository.findByIdAndUser(orderId,user).orElse(null);
        if(check==null){
            return false;
        }else{
            return true;
        }

    }
    //manager
    public Order updateStageOrder(UpdateStageOrderRequest request, Principal principal){
        Order order=getOrderById(request.getOrderId(),principal);
        order.setStage(request.getStage());
        return orderRepository.save(order);
    }
    //customer
    public OrderResponseUpdate updateOrder(UUID article,Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order order=orderRepository.findByIdAndUser(article,user).orElseThrow(()->new OrderNotFoundException("Order not found"));
        if(order.getStage().equals(EStage.STAGE_NEW)){
            List<OrderedProduct> orderedProducts=orderedProductRepository.findAllByOrder(order).orElseThrow(()->new OrderedProductNotFoundException("Заказ продукта не найден"));
            order.setPrice(priceService.orderPrice(orderedProducts));
            order.setCreatedDate(order.getCreatedDate());
            Order updatedOrder=orderRepository.save(order);
            OrderResponseUpdate orderResponseUpdate=new OrderResponseUpdate();
            orderResponseUpdate.setHttpStatus(HttpStatus.OK);
            orderResponseUpdate.setMessage("Заказ успешно обновлён");
            orderResponseUpdate.setBody(orderFacade.orderToOrderDTO(updatedOrder));
            return orderResponseUpdate;
        }else{
            OrderResponseUpdate orderResponseUpdate=new OrderResponseUpdate();
            orderResponseUpdate.setBody(null);
            orderResponseUpdate.setMessage("заказ не обновлён. Причина: он находится в стадии "+order.getStage());
            orderResponseUpdate.setHttpStatus(HttpStatus.BAD_REQUEST);
            return orderResponseUpdate;
        }
    }
    //customer
    public OrderResponseDelete deleteOrder(UUID orderId, Principal principal){
        User user=principalService.getUserByPrincipal(principal);
        Order order=orderRepository.findByIdAndUser(orderId,user).orElseThrow(()->new OrderNotFoundException("Order not found"));
        boolean isNew=order.getStage().equals(EStage.STAGE_NEW);
        if(isNew){
            order.setStage(EStage.STAGE_CANCELLED);
            orderRepository.save(order);
            OrderResponseDelete orderResponseDelete =new OrderResponseDelete();
            orderResponseDelete.setMessage("Заказ успешно удалён");
            orderResponseDelete.setHttpStatus(HttpStatus.OK);
            return orderResponseDelete;
        }else{
            OrderResponseDelete orderResponseDelete =new OrderResponseDelete();
            orderResponseDelete.setHttpStatus(HttpStatus.BAD_REQUEST);
            orderResponseDelete.setMessage("заказ не удалён. Причина: он находится в стадии "+order.getStage());
            return orderResponseDelete;
        }
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
}