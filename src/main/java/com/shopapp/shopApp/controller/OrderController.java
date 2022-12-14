package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.OrderResponse;
import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.exception.product.ShoppingCartNotFoundException;
import com.shopapp.shopApp.mapper.CartItemMapper;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.service.order.OrderServiceImpl;
import com.shopapp.shopApp.service.shoppingcart.ShoppingCartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.shopapp.shopApp.constants.ResponseConstants.ORDER_DELETED;
import static com.shopapp.shopApp.mapper.CartItemMapper.mapToDtoList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.GONE;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderServiceImpl orderService;
    private final ShoppingCartServiceImpl shoppingCartService;

    @GetMapping("/show")
    public ResponseEntity<OrderResponse> showOrder(@RequestParam String orderCode) {
        UserOrder order = orderService.getOrder(orderCode);
        ShoppingCart cart = order.getCart();
        AppUser user = cart.getUser();
        return ResponseEntity.ok(
                new OrderResponse(
                        user.getName(),
                        user.getLastName(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getEmail(),
                        mapToDtoList(cart.getItems()),
                        order.getTotalPrice()
                )
        );
    }

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
                mapToDtoList(shoppingCart.getItems()),
                shoppingCart.getTotalPrice()
        ));
    }

    @PostMapping("/complete")
    public ResponseEntity<?> completeOrder(@RequestParam String orderCode) throws OrderNotFoundException{
        orderService.completeOrder(orderCode);
        return ResponseEntity.status(CREATED).body("ORDER COMPLETED");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestParam String orderCode, @RequestBody UserOrderDto userOrderDto) {
        return null; //TODO: when ui
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@RequestParam String orderCode) throws OrderNotFoundException {
        orderService.deleteOrder(orderCode);
        return ResponseEntity.status(GONE).body(String.format(ORDER_DELETED, orderCode));
    }
}
