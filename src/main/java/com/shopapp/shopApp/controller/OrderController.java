package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.OrderResponse;
import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.mapper.CartItemMapper;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.service.order.OrderServiceImpl;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
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
    public ResponseEntity<OrderResponse> createOrder(@RequestParam String shoppingCartCode) throws ShoppingCartNotFoundException {

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCart(shoppingCartCode);
        orderService.createOrder(shoppingCart);
        AppUser user = shoppingCart.getUser();
        return ResponseEntity.status(CREATED).body(new OrderResponse(
                user.getName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getEmail(),
                shoppingCart.getItems().stream().map(CartItemMapper::mapToDto).toList(),
                shoppingCart.getTotalPrice()
        ));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestParam String orderCode, @RequestBody UserOrderDto userOrderDto) {
        return null; //TODO: when ui
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@RequestParam String orderCode) throws OrderNotFoundException {
        orderService.deleteOrder(orderCode);
        return ResponseEntity.status(GONE).body("Deleted order!");
    }
}
