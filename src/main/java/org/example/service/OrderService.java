package org.example.service;

import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getCart(User user) {
        return orderRepository.findByCustomerAndStatus(user, "CART");
    }

    public Double getCartTotal(User user) {
        Double total = orderRepository.getCartTotal(user);
        return total != null ? total : 0.0;
    }

    @Transactional
    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Товар не найден"));
        if (!product.isActive()) throw new IllegalStateException("Товар недоступен");

        Order order = orderRepository
                .findByCustomerAndProductIdAndStatus(user, productId, "CART")
                .orElse(null);

        if (order != null) {
            order.setQuantity(order.getQuantity() + quantity);
        } else {
            order = new Order();
            order.setCustomer(user);
            order.setProduct(product);
            order.setQuantity(quantity);
            order.setStatus("CART");
        }
        orderRepository.save(order);
    }

    @Transactional
    public void removeFromCart(User user, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Позиция не найдена"));
        if (!order.getCustomer().getId().equals(user.getId())) {
            throw new SecurityException("Нет доступа");
        }
        orderRepository.delete(order);
    }

    @Transactional
    public void checkout(User user) {
        List<Order> cart = getCart(user);
        if (cart.isEmpty()) {
            throw new IllegalStateException("Корзина пуста");
        }
        cart.forEach(order -> order.setStatus("PENDING"));
        orderRepository.saveAll(cart);
        log.info("Оформление заказа для пользователя: {}", user.getEmail());
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByCustomerOrderByCreatedAtDesc(user);
    }
}
