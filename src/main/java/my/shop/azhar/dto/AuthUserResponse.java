package my.shop.azhar.dto;

import my.shop.azhar.enums.UserRole;

public record AuthUserResponse(
        Long id,
        String fullName,
        String email,
        UserRole role
) {
}
