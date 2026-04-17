package my.shop.azhar.dto;

import java.time.Instant;

public record BannerResponse(
        Long id,
        String title,
        String imageUrl,
        String link,
        boolean isActive,
        Integer sortOrder,
        Instant createdAt,
        Instant updatedAt
) {
}
