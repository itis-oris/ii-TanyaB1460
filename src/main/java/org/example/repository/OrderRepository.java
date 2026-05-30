package org.example.repository;

import org.example.model.Order;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerAndStatus(User customer, String status);

    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);

    Optional<Order> findByCustomerAndProductIdAndStatus(User customer, Long productId, String status);

    @Query("SELECT SUM(o.quantity * o.product.price) FROM Order o WHERE o.customer = :customer AND o.status = 'CART'")
    Double getCartTotal(@Param("customer") User customer);
}
