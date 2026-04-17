package my.shop.azhar.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        AuthUserResponse user
) {
}
