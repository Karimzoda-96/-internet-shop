package my.shop.azhar.repository;

import my.shop.azhar.entity.Order;
import my.shop.azhar.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByStatus(OrderStatus status);
}
