package my.shop.azhar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotNull Long categoryId,
        @NotBlank @Size(max = 255) String name,
        @Size(max = 255) String slug,
        @Size(max = 4000) String description,
        @NotNull @Positive BigDecimal price,
        @Positive BigDecimal oldPrice,
        @Size(max = 255) String sku,
        @NotNull @PositiveOrZero Integer stockQuantity,
        @Size(max = 255) String barcode,
        @Size(max = 1000) String mainImageUrl,
        Boolean isActive,
        Boolean isFeatured,
        Boolean isNew
) {
}
