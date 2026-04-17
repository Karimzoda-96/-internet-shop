package my.shop.azhar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.OrderCreateRequest;
import my.shop.azhar.dto.OrderResponse;
import my.shop.azhar.dto.OrderStatusUpdateRequest;
import my.shop.azhar.dto.PageResponse;
import my.shop.azhar.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/api/admin/orders")
    public PageResponse<OrderResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sort) {
        return orderService.getOrders(page, size, sort);
    }

    @GetMapping("/api/admin/orders/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getById(id);
    }

    @PutMapping("/api/admin/orders/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusUpdateRequest request) {
        return orderService.updateStatus(id, request);
    }
}
