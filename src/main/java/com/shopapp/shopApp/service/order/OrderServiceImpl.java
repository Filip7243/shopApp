package com.shopapp.shopApp.service.order;

import com.shopapp.shopApp.dto.UserOrderDto;
import com.shopapp.shopApp.exception.order.OrderNotFoundException;
import com.shopapp.shopApp.model.CartItem;
import com.shopapp.shopApp.model.ShoppingCart;
import com.shopapp.shopApp.model.UserOrder;
import com.shopapp.shopApp.repository.CartItemRepository;
import com.shopapp.shopApp.repository.OrderRepository;
import com.shopapp.shopApp.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.shopapp.shopApp.constants.ExceptionsConstants.ORDER_NOT_FOUND;
import static com.shopapp.shopApp.mapper.OrderMapper.mapToOrder;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository itemRepository;

    @Override
    public void createOrder(ShoppingCart shoppingCart) {
        orderRepository.save(mapToOrder(shoppingCart));
    }

    // only for super-admin
    @Override
    public void updateOrder(String shoppingCartCode, UserOrderDto orderDto) {
//        UserOrder foundOrder = orderRepository.findByOrderCode(shoppingCartCode)
//                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, shoppingCartCode)));
//        foundOrder.setOrderedItems(orderDto.getItems());
//        orderRepository.save(foundOrder);
    }

    @Override
    public void deleteOrder(String shoppingCartCode) {
        orderRepository.deleteByOrderCode(shoppingCartCode)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, shoppingCartCode)));
    }

    @Override
    public UserOrder getOrder(String orderCode) {
        return orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderCode)));
    }

    @Override
    public void completeOrder(String orderCode) {
        UserOrder order = getOrder(orderCode);
        ShoppingCart cart = order.getCart();

        List<CartItem> items = cart.getItems();
        items.forEach(item ->  item.getProduct().setInStock(item.getProduct().getInStock() - item.getQuantity()));
        order.setHasPaid(true);
        itemRepository.deleteAllByCartId(cart.getId());
        items.clear();
        cart.setTotalPrice(0.0);
        orderRepository.save(order);
        shoppingCartRepository.save(cart);
    }

    private void archiveOrder(String orderCode) {
        //todo; logic to check if products are delivered to user
        UserOrder order = getOrder(orderCode);
        if(order.getIsDelivered()) {
//            mapToArchiveOrder(order);
            orderRepository.save(order);
        }
    }
}
