package my.shop.azhar.dto;

import java.time.Instant;

public record ProductImageResponse(
        Long id,
        String imageUrl,
        Integer sortOrder,
        Instant createdAt
) {
}
