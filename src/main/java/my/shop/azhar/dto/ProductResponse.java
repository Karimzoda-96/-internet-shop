package my.shop.azhar.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ProductResponse(
        Long id,
        CategoryResponse category,
        String name,
        String slug,
        String description,
        BigDecimal price,
        BigDecimal oldPrice,
        boolean discounted,
        String sku,
        Integer stockQuantity,
        String barcode,
        String mainImageUrl,
        boolean isActive,
        boolean isFeatured,
        boolean isNew,
        List<ProductImageResponse> images,
        Instant createdAt,
        Instant updatedAt
) {
}
