package my.shop.azhar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record BannerRequest(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 1000) String imageUrl,
        @Size(max = 1000) String link,
        Boolean isActive,
        @NotNull @PositiveOrZero Integer sortOrder
) {
}
