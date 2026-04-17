package my.shop.azhar.service;

import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.DashboardResponse;
import my.shop.azhar.enums.OrderStatus;
import my.shop.azhar.repository.CategoryRepository;
import my.shop.azhar.repository.OrderRepository;
import my.shop.azhar.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Transactional(readOnly = true)
    public DashboardResponse getDashboard() {
        return new DashboardResponse(
                productRepository.countByIsActiveTrue(),
                categoryRepository.countByIsActiveTrue(),
                orderRepository.count(),
                orderRepository.countByStatus(OrderStatus.NEW),
                orderService.latestOrders(5)
        );
    }
}
