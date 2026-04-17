package my.shop.azhar.dto;

import java.util.List;

public record DashboardResponse(
        long totalProducts,
        long totalCategories,
        long totalOrders,
        long newOrders,
        List<OrderResponse> latestOrders
) {
}
