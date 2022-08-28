package com.example.utro.web;

import com.example.utro.dto.OrderDTO;
import com.example.utro.entity.Order;
import com.example.utro.entity.enums.EStage;
import com.example.utro.exceptions.OrderedProductNotFoundException;
import com.example.utro.facade.OrderFacade;
import com.example.utro.payload.request.CustomerOrderRequest;
import com.example.utro.payload.request.UpdateStageOrderRequest;
import com.example.utro.payload.response.MessageResponse;
import com.example.utro.payload.response.OrderResponse;
import com.example.utro.payload.response.OrderResponseDelete;
import com.example.utro.payload.response.OrderResponseUpdate;
import com.example.utro.repository.OrderRepository;
import com.example.utro.service.OrderService;
import com.example.utro.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderFacade orderFacade;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ResponseErrorValidation responseErrorValidation;


    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> createOrder(Principal principal){
        Order createdOrder;
        try {
            createdOrder = orderService.createOrder(principal);
        }catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        OrderDTO orderDTO=orderFacade.orderToOrderDTO(createdOrder);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @GetMapping("/get/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<Object> getOrderById(@PathVariable("orderId") UUID orderId, Principal principal){
        Order order=orderService.getOrderById(orderId,principal);
        OrderDTO orderDTO=orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @GetMapping("/get/all")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('MANAGER')")
    public ResponseEntity<Object> getAllOrders(Principal principal){
        List<Order> orders=orderService.getAllOrders(principal);
        List<OrderDTO> orderDTOList=orderFacade.ordersListToOrderDTOList(orders);
        return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
    }

    @GetMapping("/get/any/all")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Object> getAnyAllOrders(){
        List<Order> orders=orderService.getAllOrders();
        List<OrderDTO> orderDTOList=orderFacade.ordersListToOrderDTOList(orders);
        return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
    }

    @PostMapping("/update/{article}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> updateOrder(@PathVariable("article") UUID article,Principal principal){
        OrderResponseUpdate response;
        try{
            response=orderService.updateOrder(article,principal);
        }
        catch (Exception e){
            return new ResponseEntity<>(new MessageResponse(e.getMessage()),HttpStatus.BAD_REQUEST);
        }
        if(response.getHttpStatus().equals(HttpStatus.BAD_REQUEST)){
            return new ResponseEntity<>(response.getMessage(),response.getHttpStatus());
        }else{
            return new ResponseEntity<>(response.getBody(),response.getHttpStatus());
        }
    }
    @PostMapping("/delete/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteOrder(@PathVariable("orderId") UUID orderId,Principal principal){
        OrderResponseDelete response =orderService.deleteOrder(orderId,principal);
        return new ResponseEntity<>(new MessageResponse(response.getMessage()),response.getHttpStatus());
    }
    @PostMapping("/delete/forever/{orderId}")
    @PreAuthorize("hasRole('DIRECTOR')")
    public ResponseEntity<Object> deleteOrderForever(@PathVariable("orderId") UUID orderId){
        OrderResponseDelete response =orderService.deleteOrderForever(orderId);
        return new ResponseEntity<>(new MessageResponse(response.getMessage()),response.getHttpStatus());
    }
    @PostMapping("/delete/aftermath/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> deleteOrderWithoutOrderedProduct(@PathVariable("orderId") UUID orderId,Principal principal){
        OrderResponseDelete response =orderService.deleteOrderWithoutOrderedProduct(orderId,principal);
        return new ResponseEntity<>(new MessageResponse(response.getMessage()),response.getHttpStatus());
    }
    @PostMapping("/add/manager/{orderId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> addManagerToOrder(@PathVariable("orderId") UUID orderId,Principal principal){

        if(orderService.checkManager(orderId,principal)){
            return new ResponseEntity<>(new MessageResponse("Вы уже присутсвуете в данном заказе"),HttpStatus.BAD_REQUEST);
        }
        Order order=orderService.addManager(orderId,principal);
        OrderDTO orderDTO=orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @PostMapping("/update/stage")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> updateStageOrder(@Valid @RequestBody UpdateStageOrderRequest request,BindingResult bindingResult, Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) return errors;
        Order order=orderService.updateStageOrder(request,principal);
        OrderDTO orderDTO=orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @PostMapping("/confirm/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Object> confirmOrder(@PathVariable("orderId") UUID orderId, Principal principal){
        Order order=orderService.confirmOrder(orderId,principal);
        OrderDTO orderDTO=orderFacade.orderToOrderDTO(order);
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }
    @GetMapping("/get/notTaken/all")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Object> getNotTakenAllOrders(Principal principal){
        List<Order> orders=orderService.getAnyAllOrders(principal);
        List<OrderDTO> orderDTOList=orderFacade.ordersListToOrderDTOList(orders);
        return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
    }
}