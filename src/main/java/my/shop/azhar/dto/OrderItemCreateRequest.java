package my.shop.azhar.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemCreateRequest(
        @NotNull Long productId,
        @NotNull @Positive Integer quantity
) {
}
