package my.shop.azhar.mapper;

import my.shop.azhar.dto.OrderItemResponse;
import my.shop.azhar.dto.OrderResponse;
import my.shop.azhar.entity.Order;
import my.shop.azhar.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    public OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getPhone(),
                order.getEmail(),
                order.getAddress(),
                order.getComment(),
                order.getDeliveryMethod(),
                order.getPaymentMethod(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems().stream().map(this::toItemResponse).toList(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getPrice(),
                item.getTotal()
        );
    }
}
