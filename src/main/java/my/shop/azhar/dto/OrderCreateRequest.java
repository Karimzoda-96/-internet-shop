package my.shop.azhar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record OrderCreateRequest(
        @NotBlank @Size(max = 255) String customerName,
        @NotBlank @Size(max = 50) String phone,
        @Email @Size(max = 255) String email,
        @NotBlank @Size(max = 1000) String address,
        @Size(max = 2000) String comment,
        @NotBlank @Size(max = 100) String deliveryMethod,
        @NotBlank @Size(max = 100) String paymentMethod,
        @NotEmpty @Valid List<OrderItemCreateRequest> items
) {
}
