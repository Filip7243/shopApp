package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.service.OrderServiceImpl;
import com.shopapp.shopApp.service.ShoppingCartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.GONE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final ShoppingCartServiceImpl shoppingCartService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestParam String shoppingCartCode) {
        try {
            orderService.createOrder(shoppingCartService.getShoppingCart(shoppingCartCode));
            return ResponseEntity.status(CREATED).body("Order created!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestParam String orderCode, @RequestBody UserOrderDto userOrderDto) {
        return null; //TODO: when ui
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@RequestParam String orderCode) {
        try {
            orderService.deleteOrder(orderCode);
            return ResponseEntity.status(GONE).body("Deleted order!");
        } catch (OrderNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
