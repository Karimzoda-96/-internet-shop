package my.shop.azhar.dto;

import java.time.Instant;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        String imageUrl,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {
}
