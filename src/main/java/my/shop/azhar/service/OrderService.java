package my.shop.azhar.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.OrderCreateRequest;
import my.shop.azhar.dto.OrderItemCreateRequest;
import my.shop.azhar.dto.OrderResponse;
import my.shop.azhar.dto.OrderStatusUpdateRequest;
import my.shop.azhar.dto.PageResponse;
import my.shop.azhar.entity.Order;
import my.shop.azhar.entity.OrderItem;
import my.shop.azhar.entity.Product;
import my.shop.azhar.enums.OrderStatus;
import my.shop.azhar.exception.BadRequestException;
import my.shop.azhar.exception.NotFoundException;
import my.shop.azhar.mapper.OrderMapper;
import my.shop.azhar.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse create(OrderCreateRequest request) {
        Order order = Order.builder()
                .customerName(request.customerName())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .comment(request.comment())
                .deliveryMethod(request.deliveryMethod())
                .paymentMethod(request.paymentMethod())
                .status(OrderStatus.NEW)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemCreateRequest itemRequest : request.items()) {
            if (itemRequest.quantity() == null || itemRequest.quantity() <= 0) {
                throw new BadRequestException("Item quantity must be greater than zero");
            }
            Product product = productService.findActiveById(itemRequest.productId());
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(itemRequest.quantity())
                    .price(product.getPrice())
                    .total(itemTotal)
                    .build();
            order.getItems().add(item);
            total = total.add(itemTotal);
        }
        order.setTotalAmount(total);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrders(int page, int size, String sort) {
        Page<OrderResponse> orders = orderRepository.findAll(PageRequest.of(
                        Math.max(page, 0),
                        Math.min(Math.max(size, 1), 100),
                        parseSort(sort)))
                .map(orderMapper::toResponse);
        return PageResponse.from(orders);
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {
        return orderMapper.toResponse(findById(id));
    }

    @Transactional
    public OrderResponse updateStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = findById(id);
        order.setStatus(request.status());
        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> latestOrders(int limit) {
        return orderRepository.findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"))).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    private Sort parseSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by(Sort.Direction.DESC, "createdAt");
        }
        String[] parts = sort.split(",");
        String property = parts[0].trim();
        if (!List.of("createdAt", "totalAmount", "status").contains(property)) {
            throw new BadRequestException("Unsupported sort field");
        }
        Sort.Direction direction = parts.length > 1 && "asc".equalsIgnoreCase(parts[1].trim())
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        return Sort.by(direction, property);
    }
}
