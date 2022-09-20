package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.CartItemDto;
import com.shopapp.shopApp.dto.OrderResponse;
import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.mapper.CartItemMapper;
import com.shopapp.shopApp.model.*;
import com.shopapp.shopApp.service.AppUserServiceImpl;
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
    private final AppUserServiceImpl appUserService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestParam String shoppingCartCode) {
        try {
            ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(shoppingCartCode);
            orderService.createOrder(shoppingCart);
            AppUser user = shoppingCart.getUser();
            return ResponseEntity.status(CREATED)
                    .body(new OrderResponse(
                            user.getName(),
                            user.getLastName(),
                            user.getPhoneNumber(),
                            user.getAddress(),
                            user.getEmail(),
                            shoppingCart.getItems().stream().map(CartItemMapper::mapToDto).toList(),
                            shoppingCart.getTotalPrice()
                    ));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null; //TODO: complete order (when ui)
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
