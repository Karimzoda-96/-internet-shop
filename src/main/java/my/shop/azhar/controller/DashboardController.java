package my.shop.azhar.controller;

import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.DashboardResponse;
import my.shop.azhar.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/api/admin/dashboard")
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }
}
