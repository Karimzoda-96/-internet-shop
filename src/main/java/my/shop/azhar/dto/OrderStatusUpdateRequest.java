package my.shop.azhar.dto;

import jakarta.validation.constraints.NotNull;
import my.shop.azhar.enums.OrderStatus;

public record OrderStatusUpdateRequest(
        @NotNull OrderStatus status
) {
}
