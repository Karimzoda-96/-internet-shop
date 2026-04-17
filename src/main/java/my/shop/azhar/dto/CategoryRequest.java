package my.shop.azhar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 255) String slug,
        @Size(max = 2000) String description,
        @Size(max = 1000) String imageUrl,
        Boolean isActive
) {
}
