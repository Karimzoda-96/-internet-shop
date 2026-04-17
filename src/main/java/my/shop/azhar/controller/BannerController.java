package my.shop.azhar.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import my.shop.azhar.dto.BannerRequest;
import my.shop.azhar.dto.BannerResponse;
import my.shop.azhar.service.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("/api/banners")
    public List<BannerResponse> getBanners() {
        return bannerService.getActiveBanners();
    }

    @PostMapping("/api/admin/banners")
    @ResponseStatus(HttpStatus.CREATED)
    public BannerResponse createBanner(@Valid @RequestBody BannerRequest request) {
        return bannerService.create(request);
    }

    @PutMapping("/api/admin/banners/{id}")
    public BannerResponse updateBanner(@PathVariable Long id, @Valid @RequestBody BannerRequest request) {
        return bannerService.update(id, request);
    }

    @DeleteMapping("/api/admin/banners/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBanner(@PathVariable Long id) {
        bannerService.delete(id);
    }
}
