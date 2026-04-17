package my.shop.azhar.dto;

import java.math.BigDecimal;

public record ProductListItemResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String name,
        String slug,
        BigDecimal price,
        BigDecimal oldPrice,
        boolean discounted,
        String sku,
        Integer stockQuantity,
        String mainImageUrl,
        boolean isFeatured,
        boolean isNew
) {
}
