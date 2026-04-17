package my.shop.azhar.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import my.shop.azhar.enums.OrderStatus;

public record OrderResponse(
        Long id,
        String customerName,
        String phone,
        String email,
        String address,
        String comment,
        String deliveryMethod,
        String paymentMethod,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {
}
